package no.nav.personbruker.dittnav.api.oppgave

import io.kotest.matchers.shouldBe
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.respondError
import io.ktor.client.features.HttpTimeout
import io.ktor.client.features.json.JsonFeature
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import no.nav.personbruker.dittnav.api.config.json
import no.nav.personbruker.dittnav.api.tokenx.AccessToken
import no.nav.personbruker.dittnav.api.util.createBasicMockedHttpClient
import org.junit.jupiter.api.Test
import java.net.URL

internal class OppgaveConsumerTest {

    private val dummyToken = AccessToken("<access_token>")

    @Test
    fun `should call oppgave endpoint on event handler`() {

        val client = HttpClient(MockEngine) {
            install(HttpTimeout) {
                requestTimeoutMillis = 500
            }

            engine {
                addHandler { request ->
                    if (request.url.encodedPath.contains("/fetch/oppgave") && request.url.host.contains("event-handler")) {
                        respond("[]", headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString()))
                    } else {
                        respondError(HttpStatusCode.BadRequest)
                    }
                }
            }
            install(JsonFeature)
        }

        val oppgaveConsumer = OppgaveConsumer(client, URL("http://event-handler"))

        runBlocking {
            oppgaveConsumer.getExternalActiveEvents(dummyToken) shouldBe emptyList()
        }
    }

    @Test
    fun `should get list of active Oppgave`() {
        val oppgaveObject1 = createOppgave("1", "1", true)
        val oppgaveObject2 = createOppgave("2", "2", true)

        val client = createBasicMockedHttpClient {
            respond(
                    json().encodeToString(listOf(oppgaveObject1, oppgaveObject2)),
                    headers = headersOf(HttpHeaders.ContentType,
                            ContentType.Application.Json.toString())
            )
        }
        val oppgaveConsumer = OppgaveConsumer(client, URL("http://event-handler"))

        runBlocking {
            val externalActiveEvents = oppgaveConsumer.getExternalActiveEvents(dummyToken)
            val event = externalActiveEvents.first()
            externalActiveEvents.size shouldBe 2
            event.tekst shouldBe oppgaveObject1.tekst
            event.fodselsnummer shouldBe oppgaveObject1.fodselsnummer
            event.aktiv shouldBe true
        }
    }

    @Test
    fun `should get list of inactive Oppgave`() {
        val oppgaveObject = createOppgave("1", "1", false)

        val client = createBasicMockedHttpClient {
            respond(
                    json().encodeToString(listOf(oppgaveObject)),
                    headers = headersOf(HttpHeaders.ContentType,
                            ContentType.Application.Json.toString())
            )
        }
        val oppgaveConsumer = OppgaveConsumer(client, URL("http://event-handler"))

        runBlocking {
            val externalInactiveEvents = oppgaveConsumer.getExternalInactiveEvents(dummyToken)
            val event = externalInactiveEvents.first()
            externalInactiveEvents.size shouldBe 1
            event.tekst shouldBe oppgaveObject.tekst
            event.fodselsnummer shouldBe oppgaveObject.fodselsnummer
            event.aktiv shouldBe false
        }
    }
}
