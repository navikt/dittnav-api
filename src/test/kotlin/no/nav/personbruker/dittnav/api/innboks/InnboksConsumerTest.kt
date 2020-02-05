package no.nav.personbruker.dittnav.api.innboks

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
import no.nav.personbruker.dittnav.api.common.createInnloggetBruker
import no.nav.personbruker.dittnav.api.config.buildJsonSerializer
import no.nav.personbruker.dittnav.api.config.enableDittNavJsonConfig
import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.`should equal`
import org.junit.jupiter.api.Test
import java.net.URL

class InnboksConsumerTest {

    val innloggetBruker = createInnloggetBruker()

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
            install(JsonFeature) {
                serializer = buildJsonSerializer()
            }
        }
        val innboksConsumer = InnboksConsumer(client, URL("http://event-handler"))

        runBlocking {
            innboksConsumer.getExternalEvents(innloggetBruker) `should equal` emptyList()
        }

    }

    @Test
    fun `should get list of Innboks`() {
        val innboksObject1 = createInnboks("1", "1")
        val innboksObject2 = createInnboks("2", "2")

        val objectMapper = ObjectMapper().apply {
            enableDittNavJsonConfig()
        }

        val client = HttpClient(MockEngine) {
            engine {
                addHandler {
                    respond(
                            objectMapper.writeValueAsString(listOf(innboksObject1, innboksObject2)),
                            headers = headersOf(HttpHeaders.ContentType,
                                    ContentType.Application.Json.toString())
                    )
                }
            }
            install(JsonFeature) {
                serializer = buildJsonSerializer()
            }
        }
        val innboksConsumer = InnboksConsumer(client, URL("http://event-handler"))

        runBlocking {
            innboksConsumer.getExternalEvents(innloggetBruker).size `should be equal to` 2
            innboksConsumer.getExternalEvents(innloggetBruker)[0].tekst `should be equal to` innboksObject1.tekst
            innboksConsumer.getExternalEvents(innloggetBruker)[0].fodselsnummer `should be equal to` innboksObject1.fodselsnummer
        }
    }
}
