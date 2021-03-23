package no.nav.personbruker.dittnav.api.beskjed

import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import no.nav.personbruker.dittnav.api.common.AuthenticatedUserObjectMother
import no.nav.personbruker.dittnav.api.config.json
import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.`should be false`
import org.amshove.kluent.`should be true`
import org.junit.jupiter.api.Test
import java.net.URL

class BeskjedConsumerTest {

    val user = AuthenticatedUserObjectMother.createAuthenticatedUser()

    @Test
    fun `Skal kalle beskjed-endepunktet i event-handler`() {
        val client = HttpClient(MockEngine) {
            engine {
                addHandler { request ->
                    if (request.url.encodedPath.contains("/fetch/beskjed") && request.url.host.contains("event-handler")) {
                        respond("[]", headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString()))
                    } else {
                        respondError(HttpStatusCode.BadRequest)
                    }
                }
            }
            install(JsonFeature)
        }
        val beskjedConsumer = BeskjedConsumer(client, URL("http://event-handler"))

        runBlocking {
            beskjedConsumer.getExternalActiveEvents(user) `should be equal to` emptyList()
        }
    }

    @Test
    fun `Skal motta en liste over aktive Beskjeder`() {
        val beskjedObject = createBeskjed("1", "1", "1", true)
        val client = getClient {
            respond(
                    json().encodeToString(listOf(beskjedObject)),
                    headers = headersOf(HttpHeaders.ContentType,
                            ContentType.Application.Json.toString())
            )
        }
        val beskjedConsumer = BeskjedConsumer(client, URL("http://event-handler"))

        runBlocking {
            val externalActiveEvents = beskjedConsumer.getExternalActiveEvents(user)
            val event = externalActiveEvents.first()
            externalActiveEvents.size `should be equal to` 1
            event.tekst `should be equal to` beskjedObject.tekst
            event.fodselsnummer `should be equal to` beskjedObject.fodselsnummer
            event.aktiv.`should be true`()
        }
    }

    @Test
    fun `Skal motta en liste over inaktive Beskjeder`() {
        val beskjedObject = createBeskjed("1", "1", "1", false)

        val client = getClient {
            respond(
                    json().encodeToString(listOf(beskjedObject)),
                    headers = headersOf(HttpHeaders.ContentType,
                            ContentType.Application.Json.toString())
            )
        }
        val beskjedConsumer = BeskjedConsumer(client, URL("http://event-handler"))

        runBlocking {
            val externalInactiveEvents = beskjedConsumer.getExternalInactiveEvents(user)
            val event = externalInactiveEvents.first()
            externalInactiveEvents.size `should be equal to` 1
            event.tekst `should be equal to` beskjedObject.tekst
            event.fodselsnummer `should be equal to` beskjedObject.fodselsnummer
            event.aktiv.`should be false`()
        }
    }

    private fun getClient(respond: MockRequestHandleScope.() -> HttpResponseData): HttpClient {
        return HttpClient(MockEngine) {
            engine {
                addHandler {
                    respond()
                }
            }
            install(JsonFeature)
        }
    }
}
