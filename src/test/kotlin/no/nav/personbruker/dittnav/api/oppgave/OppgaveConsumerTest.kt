package no.nav.personbruker.dittnav.api.oppgave

import com.fasterxml.jackson.databind.ObjectMapper
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.respondError
import io.ktor.client.features.json.JsonFeature
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.runBlocking
import no.nav.personbruker.dittnav.api.common.InnloggetBrukerObjectMother
import no.nav.personbruker.dittnav.api.config.buildJsonSerializer
import no.nav.personbruker.dittnav.api.config.enableDittNavJsonConfig
import org.amshove.kluent.`should be equal to`
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
            oppgaveConsumer.getExternalEvents(innloggetBruker) `should equal` emptyList()
        }

    }

    @Test
    fun `should get list of Oppgave`() {

        val oppgaveObject1 = createOppgave("1", "1")
        val oppgaveObject2 = createOppgave("2", "2")
        val objectMapper = ObjectMapper().apply {
            enableDittNavJsonConfig()
        }

        val client = HttpClient(MockEngine) {
            engine {
                addHandler {
                    respond(
                            objectMapper.writeValueAsString(listOf(oppgaveObject1, oppgaveObject2)),
                            headers = headersOf(HttpHeaders.ContentType,
                                    ContentType.Application.Json.toString())
                    )
                }
            }
            install(JsonFeature) {
                serializer = buildJsonSerializer()
            }
        }
        val oppgaveConsumer = OppgaveConsumer(client, URL("http://event-handler"))

        runBlocking {
            oppgaveConsumer.getExternalEvents(innloggetBruker).size `should be equal to` 2
            oppgaveConsumer.getExternalEvents(innloggetBruker)[0].tekst `should be equal to` oppgaveObject1.tekst
            oppgaveConsumer.getExternalEvents(innloggetBruker)[0].fodselsnummer `should be equal to` oppgaveObject1.fodselsnummer
        }

    }
}
