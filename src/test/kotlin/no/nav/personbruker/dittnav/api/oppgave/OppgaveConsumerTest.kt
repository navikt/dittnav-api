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
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import no.nav.personbruker.dittnav.api.config.*
import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.`should equal`
import org.junit.jupiter.api.Test
import java.net.URL

class OppgaveConsumerTest {

    val httpClientBuilder = mockk<HttpClientBuilder>(relaxed = true)
    val oppgaveConsumer = OppgaveConsumer(httpClientBuilder, URL("http://event-handler"))

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
        every { httpClientBuilder.build() } returns client

        runBlocking {
            oppgaveConsumer.getExternalEvents("1234") `should equal` emptyList()
        }

    }

    @Test
    fun `should get list of Oppgave`() {
        val oppgaveObject1 = OppgaveObjectMother.createOppgave("1", "1")
        val oppgaveObject2 = OppgaveObjectMother.createOppgave("2", "2")
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
        every { httpClientBuilder.build() } returns client

        runBlocking {
            oppgaveConsumer.getExternalEvents("1234").size `should be equal to` 2
            oppgaveConsumer.getExternalEvents("1234")[0].tekst `should be equal to` oppgaveObject1.tekst
            oppgaveConsumer.getExternalEvents("1234")[0].fodselsnummer `should be equal to` oppgaveObject1.fodselsnummer
        }

    }

}
