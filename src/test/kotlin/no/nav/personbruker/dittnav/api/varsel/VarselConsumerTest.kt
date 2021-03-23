package no.nav.personbruker.dittnav.api.varsel

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
import org.junit.jupiter.api.Test
import java.net.URL

class VarselConsumerTest {

    private val user = AuthenticatedUserObjectMother.createAuthenticatedUser()

    @Test
    fun `Skal kalle varsel-endepunktet i dittnav-legacy-api`() {
        val client = HttpClient(MockEngine) {
            engine {
                addHandler { request ->
                    if (request.url.encodedPath.contains("/varselinnboks/siste") && request.url.host.contains("dittnav-legacy-api")) {
                        respond("[]", headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString()))
                    } else {
                        respondError(HttpStatusCode.BadRequest)
                    }
                }
            }
            install(JsonFeature)
        }
        val varselConsumer = VarselConsumer(client, URL("http://dittnav-legacy-api"))

        runBlocking {
            varselConsumer.getSisteVarsler(user) `should be equal to` emptyList()
        }
    }

    @Test
    fun `Skal motta en liste av aktive Varsel`() {
        val varselObject = createLestVarsel("1")
        val client = getClient {
            respond(
                    json().encodeToString(listOf(varselObject)),
                    headers = headersOf(HttpHeaders.ContentType,
                            ContentType.Application.Json.toString())
            )
        }
        val varselConsumer = VarselConsumer(client, URL("http://dittnav-legacy-api"))

        runBlocking {
            val externalActiveEvents = varselConsumer.getSisteVarsler(user)
            val event = externalActiveEvents.first()
            externalActiveEvents.size `should be equal to` 1
            event.varseltekst `should be equal to` varselObject.varseltekst
            event.aktoerID `should be equal to` varselObject.aktoerID
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
