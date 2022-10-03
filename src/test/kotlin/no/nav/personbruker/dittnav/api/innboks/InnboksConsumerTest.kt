package no.nav.personbruker.dittnav.api.innboks

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
import no.nav.personbruker.dittnav.api.tokenx.AccessToken
import no.nav.personbruker.dittnav.api.util.applicationHttpClient
import org.junit.jupiter.api.Test
import java.net.URL

internal class InnboksConsumerTest {

    private val dummyToken = AccessToken("<access_token>")

    @Test
    fun `should call innboks endpoint on event handler`() {

        testApplication {
            val client = applicationHttpClient()
            externalServices {

                /*          TODO        if (request.url.encodedPath.contains("/fetch/innboks") && request.url.host.contains("event-handler")) {
                            respond(
                                "[]",
                                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                            )
                        } else {
                            respondError(HttpStatusCode.BadRequest)
                        }
                    }
                }
            }*/
            }

            val innboksConsumer = InnboksConsumer(client, URL("http://event-handler"))

            runBlocking {
                innboksConsumer.getExternalActiveEvents(dummyToken) shouldBe emptyList()
            }
        }
    }

    @Test
    fun `should get list of active Innboks`() {
        val innboksObject1 = createInnboks("1", "1", true)
        val innboksObject2 = createInnboks("2", "2", true)
        testApplication {

            externalServices {
                /*TODO
            jsonConfig().encodeToString(listOf(innboksObject1, innboksObject2)),
             headers = headersOf(
             HttpHeaders.ContentType,
             ContentType.Application.Json.toString()
                  */
            }
            val innboksConsumer = InnboksConsumer(applicationHttpClient(), URL("http://event-handler"))

            runBlocking {
                val externalActiveEvents = innboksConsumer.getExternalActiveEvents(dummyToken)
                val event = externalActiveEvents.first()
                externalActiveEvents.size shouldBe 2
                event.tekst shouldBe innboksObject1.tekst
                event.fodselsnummer shouldBe innboksObject1.fodselsnummer
                event.aktiv shouldBe true
            }
        }
    }

    @Test
    fun `should get list of inactive Innboks`() {
        val innboksObject = createInnboks("1", "1", false)

        testApplication {
            val innboksConsumer = InnboksConsumer(applicationHttpClient(), URL("http://event-handler"))
            externalServices {
                /*TODO

                    jsonConfig().encodeToString(listOf(innboksObject)),
                    headers = headersOf(HttpHeaders.ContentType,
                            ContentType.Application.Json.toString())
        */
            }


            runBlocking {
                val externalInactiveEvents = innboksConsumer.getExternalInactiveEvents(dummyToken)
                val event = externalInactiveEvents.first()
                externalInactiveEvents.size shouldBe 1
                event.tekst shouldBe innboksObject.tekst
                event.fodselsnummer shouldBe innboksObject.fodselsnummer
                event.aktiv shouldBe false
            }
        }
    }
}
