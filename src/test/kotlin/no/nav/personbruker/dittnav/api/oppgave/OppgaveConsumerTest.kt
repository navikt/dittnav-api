package no.nav.personbruker.dittnav.api.oppgave

import com.fasterxml.jackson.databind.ObjectMapper
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockRequestHandleScope
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.respondError
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.request.HttpResponseData
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.runBlocking
import no.nav.personbruker.dittnav.api.common.InnloggetBrukerObjectMother
import no.nav.personbruker.dittnav.api.config.buildJsonSerializer
import no.nav.personbruker.dittnav.api.config.enableDittNavJsonConfig
import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.`should be false`
import org.amshove.kluent.`should be true`
import org.amshove.kluent.`should equal`
import org.junit.jupiter.api.Test
import java.net.URL

class OppgaveConsumerTest {

    val innloggetBruker = InnloggetBrukerObjectMother.createInnloggetBruker()

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
            oppgaveConsumer.getExternalActiveEvents(innloggetBruker) `should equal` emptyList()
        }
    }

    @Test
    fun `should get list of active Oppgave`() {
        val oppgaveObject1 = createOppgave("1", "1", true)
        val oppgaveObject2 = createOppgave("2", "2", true)
        val objectMapper = ObjectMapper().apply {
            enableDittNavJsonConfig()
        }

        val client = getClient {
            respond(
                    objectMapper.writeValueAsString(listOf(oppgaveObject1, oppgaveObject2)),
                    headers = headersOf(HttpHeaders.ContentType,
                            ContentType.Application.Json.toString())
            )
        }
        val oppgaveConsumer = OppgaveConsumer(client, URL("http://event-handler"))

        runBlocking {
            val externalActiveEvents = oppgaveConsumer.getExternalActiveEvents(innloggetBruker)
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
        val objectMapper = ObjectMapper().apply {
            enableDittNavJsonConfig()
        }

        val client = getClient {
            respond(
                    objectMapper.writeValueAsString(listOf(oppgaveObject)),
                    headers = headersOf(HttpHeaders.ContentType,
                            ContentType.Application.Json.toString())
            )
        }
        val oppgaveConsumer = OppgaveConsumer(client, URL("http://event-handler"))

        runBlocking {
            val externalInactiveEvents = oppgaveConsumer.getExternalInactiveEvents(innloggetBruker)
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
