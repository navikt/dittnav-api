package no.nav.personbruker.dittnav.api.beskjed

import io.kotest.matchers.shouldBe
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.respondError
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.server.testing.testApplication
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import no.nav.personbruker.dittnav.api.config.jsonConfig
import no.nav.personbruker.dittnav.api.tokenx.AccessToken
import no.nav.personbruker.dittnav.api.util.applicationHttpClient
import org.junit.jupiter.api.Test
import java.net.URL

internal class BeskjedConsumerTest {

    private val dummyToken = AccessToken("<access_token>")

    @Test
    fun `Skal kalle beskjed-endepunktet i event-handler`() {
        testApplication {
            val beskjedConsumer = BeskjedConsumer(client, URL("http://event-handler"))
            externalServices {
                /*
                TODO

                    if (request.url.encodedPath.contains("/fetch/beskjed") && request.url.host.contains("event-handler")) {
                        respond(
                            "[]",
                            headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                        )
                    } else {
                        respondError(HttpStatusCode.BadRequest)
                    }
                */
            }

            runBlocking {
                beskjedConsumer.getExternalActiveEvents(dummyToken) shouldBe emptyList()
            }
        }
    }

    @Test
    fun `Skal motta en liste over aktive Beskjeder`() {
        val beskjedObject = createBeskjed(eventId = "1", fodselsnummer = "1", aktiv = true)
        testApplication {
            externalServices {
                /*TODO
                    jsonConfig().encodeToString(listOf(beskjedObject)),
                    headers = headersOf(
                        HttpHeaders.ContentType,
                        ContentType.Application.Json.toString()
                    )
                )*/
            }

            val beskjedConsumer = BeskjedConsumer(applicationHttpClient(), URL("http://event-handler"))

            runBlocking {
                val externalActiveEvents = beskjedConsumer.getExternalActiveEvents(dummyToken)
                val event = externalActiveEvents.first()
                externalActiveEvents.size shouldBe 1
                event.tekst shouldBe beskjedObject.tekst
                event.fodselsnummer shouldBe beskjedObject.fodselsnummer
                event.aktiv shouldBe true
            }
        }
    }

    @Test
    fun `Skal motta en liste over inaktive Beskjeder`() {
        val beskjedObject = createBeskjed(eventId = "1", fodselsnummer = "1", aktiv = false)

        testApplication {
            externalServices {

                /*jsonConfig().encodeToString(listOf(beskjedObject)),
                    headers = headersOf(
                        HttpHeaders.ContentType,
                        ContentType.Application.Json.toString()
                    )
                )*/
            }
            val beskjedConsumer = BeskjedConsumer(applicationHttpClient(), URL("http://event-handler"))

            runBlocking {
                val externalInactiveEvents = beskjedConsumer.getExternalInactiveEvents(dummyToken)
                val event = externalInactiveEvents.first()
                externalInactiveEvents.size shouldBe 1
                event.tekst shouldBe beskjedObject.tekst
                event.fodselsnummer shouldBe beskjedObject.fodselsnummer
                event.aktiv shouldBe false
            }
        }
    }

}
