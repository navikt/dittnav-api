package no.nav.personbruker.dittnav.api.beskjed

import io.kotest.matchers.shouldBe
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import io.ktor.server.testing.testApplication
import kotlinx.coroutines.runBlocking
import no.nav.personbruker.dittnav.api.mockApi
import no.nav.personbruker.dittnav.api.rawEventHandlerBeskjed
import no.nav.personbruker.dittnav.api.respondRawJson
import no.nav.personbruker.dittnav.api.tokenx.AccessToken
import no.nav.personbruker.dittnav.api.util.applicationHttpClient
import org.junit.jupiter.api.Test
import java.lang.AssertionError
import java.net.URL

internal class BeskjedConsumerTest {

    private val testEventHandlerUrl = "https://test.eventhandler.no"
    private val dummyToken = AccessToken("<access_token>")


    @Test
    fun `Skal kalle beskjed-endepunktet i event-handler`() {
        testApplication {
            val beskjedConsumer = BeskjedConsumer(applicationHttpClient(), URL(testEventHandlerUrl))
            mockApi()
            externalServices {
                hosts(testEventHandlerUrl) {
                    install(ContentNegotiation) { json() }
                    routing {
                        get("fetch/beskjed/aktive") {
                            call.respondRawJson("[]")
                        }
                    }
                }

                runBlocking {
                    beskjedConsumer.getExternalActiveEvents(dummyToken) shouldBe emptyList()
                }
            }
        }
    }

    @Test
    fun `Skal motta en liste over aktive Beskjeder`() {
        val expectedTekst = "En tekst som er forventet"
        val expectedFnr = "8177625478"
        testApplication {
            externalServices {
                hosts(testEventHandlerUrl) {
                    routing {
                        get("fetch/beskjed/aktive") {
                            call.respondRawJson(
                                "[${
                                    rawEventHandlerBeskjed(
                                        fodselsnummer = expectedFnr,
                                        tekst = expectedTekst,
                                        aktiv = true
                                    )
                                }]"
                            )
                        }
                    }
                }
            }
            val beskjedConsumer = BeskjedConsumer(applicationHttpClient(), URL(testEventHandlerUrl))

            runBlocking {
                val externalActiveEvents = beskjedConsumer.getExternalActiveEvents(dummyToken)
                val event = externalActiveEvents.first()
                externalActiveEvents.size shouldBe 1
                event.tekst shouldBe expectedTekst
                event.fodselsnummer shouldBe expectedFnr
                event.aktiv shouldBe true
            }
        }
    }

    @Test
    fun `Skal motta en liste over inaktive Beskjeder`() {
        val beskjedObject = createBeskjed(eventId = "1", fodselsnummer = "1", aktiv = false)
        val beskjedObject2 = createBeskjed(eventId = "1", fodselsnummer = "1", aktiv = false)


        testApplication {
            externalServices {
                hosts(testEventHandlerUrl) {
                    routing {
                        get("fetch/beskjed/inaktive") {
                            call.respondRawJson(
                                "[${
                                    rawEventHandlerBeskjed(
                                        fodselsnummer = beskjedObject.fodselsnummer,
                                        eventId = beskjedObject.eventId,
                                        tekst = beskjedObject.tekst,
                                        aktiv = false
                                    )
                                },${
                                    rawEventHandlerBeskjed(
                                        fodselsnummer = beskjedObject2.fodselsnummer,
                                        eventId = beskjedObject2.eventId,
                                        tekst = beskjedObject2.tekst,
                                        aktiv = false
                                    )
                                }]"
                            )
                        }
                    }
                }
            }
            val beskjedConsumer = BeskjedConsumer(applicationHttpClient(), URL(testEventHandlerUrl))

            runBlocking {
                val externalInactiveEvents = beskjedConsumer.getExternalInactiveEvents(dummyToken)
                externalInactiveEvents.size shouldBe 2
                externalInactiveEvents.find { it.eventId == beskjedObject.eventId }?.let {
                    it.tekst shouldBe beskjedObject.tekst
                    it.fodselsnummer shouldBe beskjedObject.fodselsnummer
                    it.aktiv shouldBe false
                }
                    ?: throw AssertionError("Fant ikke varsel med eventId ${beskjedObject.eventId} i liste $externalInactiveEvents")
                externalInactiveEvents.find { it.eventId == beskjedObject2.eventId }?.let {
                    it.tekst shouldBe beskjedObject2.tekst
                    it.fodselsnummer shouldBe beskjedObject2.fodselsnummer
                    it.aktiv shouldBe false
                }
                    ?: throw AssertionError("Fant ikke varsel med eventId ${beskjedObject2.eventId} i liste $externalInactiveEvents")
            }
        }
    }

}

