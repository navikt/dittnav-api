package no.nav.personbruker.dittnav.api.innboks

import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import no.nav.personbruker.dittnav.api.common.AuthenticatedUserObjectMother
import no.nav.personbruker.dittnav.api.config.json
import no.nav.personbruker.dittnav.api.util.createBasicMockedHttpClient
import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.`should be false`
import org.amshove.kluent.`should be true`
import org.junit.jupiter.api.Test
import java.net.URL

class InnboksConsumerTest {

    val user = AuthenticatedUserObjectMother.createAuthenticatedUser()

    @Test
    fun `should call innboks endpoint on event handler`() {
        val client = HttpClient(MockEngine) {
            engine {
                addHandler { request ->
                    if (request.url.encodedPath.contains("/fetch/innboks") && request.url.host.contains("event-handler")) {
                        respond("[]", headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString()))
                    } else {
                        respondError(HttpStatusCode.BadRequest)
                    }
                }
            }
            install(JsonFeature)
        }
        val innboksConsumer = InnboksConsumer(client, URL("http://event-handler"))

        runBlocking {
            innboksConsumer.getExternalActiveEvents(user) `should be equal to` emptyList()
        }
    }

    @Test
    fun `should get list of active Innboks`() {
        val innboksObject1 = createInnboks("1", "1", true)
        val innboksObject2 = createInnboks("2", "2", true)

        val client = createBasicMockedHttpClient {
            respond(
                    json().encodeToString(listOf(innboksObject1, innboksObject2)),
                    headers = headersOf(HttpHeaders.ContentType,
                            ContentType.Application.Json.toString())
            )
        }
        val innboksConsumer = InnboksConsumer(client, URL("http://event-handler"))

        runBlocking {
            val externalActiveEvents = innboksConsumer.getExternalActiveEvents(user)
            val event = externalActiveEvents.first()
            externalActiveEvents.size `should be equal to` 2
            event.tekst `should be equal to` innboksObject1.tekst
            event.fodselsnummer `should be equal to` innboksObject1.fodselsnummer
            event.aktiv.`should be true`()
        }
    }

    @Test
    fun `should get list of inactive Innboks`() {
        val innboksObject = createInnboks("1", "1", false)

        val client = createBasicMockedHttpClient {
            respond(
                    json().encodeToString(listOf(innboksObject)),
                    headers = headersOf(HttpHeaders.ContentType,
                            ContentType.Application.Json.toString())
            )
        }
        val innboksConsumer = InnboksConsumer(client, URL("http://event-handler"))

        runBlocking {
            val externalInactiveEvents = innboksConsumer.getExternalInactiveEvents(user)
            val event = externalInactiveEvents.first()
            externalInactiveEvents.size `should be equal to` 1
            event.tekst `should be equal to` innboksObject.tekst
            event.fodselsnummer `should be equal to` innboksObject.fodselsnummer
            event.aktiv.`should be false`()
        }
    }

}
