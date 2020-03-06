package no.nav.personbruker.dittnav.api.innboks

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

class InnboksConsumerTest {

    val innloggetBruker = InnloggetBrukerObjectMother.createInnloggetBruker()

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
            innboksConsumer.getExternalActiveEvents(innloggetBruker) `should equal` emptyList()
        }

    }

    @Test
    fun `should get list of active Innboks`() {
        val innboksObject1 = createInnboks("1", "1", true)
        val innboksObject2 = createInnboks("2", "2", true)

        val objectMapper = ObjectMapper().apply {
            enableDittNavJsonConfig()
        }

        val client = getClient {
            respond(
                    objectMapper.writeValueAsString(listOf(innboksObject1, innboksObject2)),
                    headers = headersOf(HttpHeaders.ContentType,
                            ContentType.Application.Json.toString())
            )
        }
        val innboksConsumer = InnboksConsumer(client, URL("http://event-handler"))

        runBlocking {
            val externalActiveEvents = innboksConsumer.getExternalActiveEvents(innloggetBruker)
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

        val objectMapper = ObjectMapper().apply {
            enableDittNavJsonConfig()
        }

        val client = getClient {
            respond(
                    objectMapper.writeValueAsString(listOf(innboksObject)),
                    headers = headersOf(HttpHeaders.ContentType,
                            ContentType.Application.Json.toString())
            )
        }
        val innboksConsumer = InnboksConsumer(client, URL("http://event-handler"))

        runBlocking {
            val externalInactiveEvents = innboksConsumer.getExternalInactiveEvents(innloggetBruker)
            val event = externalInactiveEvents.first()
            externalInactiveEvents.size `should be equal to` 1
            event.tekst `should be equal to` innboksObject.tekst
            event.fodselsnummer `should be equal to` innboksObject.fodselsnummer
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
