package no.nav.personbruker.dittnav.api.beskjed

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
import no.nav.personbruker.dittnav.api.config.Environment
import no.nav.personbruker.dittnav.api.config.HttpClientBuilder
import no.nav.personbruker.dittnav.api.config.buildJsonSerializer
import no.nav.personbruker.dittnav.api.config.enableDittNavJsonConfig
import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.`should equal`
import org.junit.jupiter.api.Test
import java.net.URL

class BeskjedConsumerTest {

    val httpClientBuilder = mockk<HttpClientBuilder>(relaxed = true)
    val beskjedConsumer = BeskjedConsumer(httpClientBuilder, Environment(URL("http://legacy-api"), URL("http://event-handler")))

    @Test
    fun `should call information endpoint on event handler`() {
        val client = HttpClient(MockEngine) {
            engine {
                addHandler { request ->
                    if (request.url.encodedPath.contains("/fetch/informasjon") && request.url.host.contains("event-handler")) {
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
            beskjedConsumer.getExternalEvents("1234") `should equal` emptyList()
        }

    }

    @Test
    fun `should get list of Beskjed`() {
        val beskjedObject = BeskjedObjectMother.createBeskjed("1", "1")
        val objectMapper = ObjectMapper().apply {
            enableDittNavJsonConfig()
        }
        val client = HttpClient(MockEngine) {
            engine {
                addHandler {
                    respond(
                            objectMapper.writeValueAsString(listOf(beskjedObject)),
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
            beskjedConsumer.getExternalEvents("1234").size `should be equal to` 1
            beskjedConsumer.getExternalEvents("1234")[0].tekst `should be equal to` beskjedObject.tekst
            beskjedConsumer.getExternalEvents("1234")[0].aktorId `should be equal to` beskjedObject.aktorId
        }

    }
}
