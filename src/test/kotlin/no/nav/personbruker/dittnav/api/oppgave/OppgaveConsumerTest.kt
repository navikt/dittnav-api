package no.nav.personbruker.dittnav.api.oppgave

import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import no.nav.personbruker.dittnav.api.common.AuthenticatedUserObjectMother
import no.nav.personbruker.dittnav.api.config.buildJsonSerializer
import no.nav.personbruker.dittnav.api.config.json
import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.`should be false`
import org.amshove.kluent.`should be true`
import org.junit.jupiter.api.Test
import java.net.URL

class OppgaveConsumerTest {

    val user = AuthenticatedUserObjectMother.createAuthenticatedUser()

    @Test
    fun `should call oppgave endpoint on event handler`() {

        val client = HttpClient(MockEngine) {
            engine {
                addHandler { request ->
                    if (request.url.encodedPath.contains("/fetch/oppgave") && request.url.host.contains("event-handler")) {
                        respond("[]", headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString()))
                    } else {
                        respondError(HttpStatusCode.BadRequest)
                    }
                }
            }
            install(JsonFeature) {
                serializer = buildJsonSerializer()
            }
        }

        val oppgaveConsumer = OppgaveConsumer(client, URL("http://event-handler"))

        runBlocking {
            oppgaveConsumer.getExternalActiveEvents(user) `should be equal to` emptyList()
        }
    }

    @Test
    fun `should get list of active Oppgave`() {
        val oppgaveObject1 = createOppgave("1", "1", true)
        val oppgaveObject2 = createOppgave("2", "2", true)

        val client = getClient {
            respond(
                    json().encodeToString(listOf(oppgaveObject1, oppgaveObject2)),
                    headers = headersOf(HttpHeaders.ContentType,
                            ContentType.Application.Json.toString())
            )
        }
        val oppgaveConsumer = OppgaveConsumer(client, URL("http://event-handler"))

        runBlocking {
            val externalActiveEvents = oppgaveConsumer.getExternalActiveEvents(user)
            val event = externalActiveEvents.first()
            externalActiveEvents.size `should be equal to` 2
            event.tekst `should be equal to` oppgaveObject1.tekst
            event.fodselsnummer `should be equal to` oppgaveObject1.fodselsnummer
            event.aktiv.`should be true`()
        }
    }

    @Test
    fun `should get list of inactive Oppgave`() {
        val oppgaveObject = createOppgave("1", "1", false)

        val client = getClient {
            respond(
                    json().encodeToString(listOf(oppgaveObject)),
                    headers = headersOf(HttpHeaders.ContentType,
                            ContentType.Application.Json.toString())
            )
        }
        val oppgaveConsumer = OppgaveConsumer(client, URL("http://event-handler"))

        runBlocking {
            val externalInactiveEvents = oppgaveConsumer.getExternalInactiveEvents(user)
            val event = externalInactiveEvents.first()
            externalInactiveEvents.size `should be equal to` 1
            event.tekst `should be equal to` oppgaveObject.tekst
            event.fodselsnummer `should be equal to` oppgaveObject.fodselsnummer
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
            install(JsonFeature) {
                serializer = buildJsonSerializer()
            }
        }
    }
}
