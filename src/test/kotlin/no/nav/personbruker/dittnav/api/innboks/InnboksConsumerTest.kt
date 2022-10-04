package no.nav.personbruker.dittnav.api.innboks

import io.kotest.matchers.shouldBe
import io.ktor.server.application.call
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import io.ktor.server.testing.testApplication
import kotlinx.coroutines.runBlocking
import no.nav.personbruker.dittnav.api.rawEventHandlerVarsel
import no.nav.personbruker.dittnav.api.respondRawJson
import no.nav.personbruker.dittnav.api.tokenx.AccessToken
import no.nav.personbruker.dittnav.api.applicationHttpClient
import org.junit.jupiter.api.Test
import java.net.URL

internal class InnboksConsumerTest {

    private val dummyToken = AccessToken("<access_token>")
    private val testEventHandlerEndpoint = "http://event-handler"

    @Test
    fun `should get list of active Innboks`() {
        val innboksObject1 = createInnboks("1", "1", true)
        val innboksObject2 = createInnboks("2", "2", true)
        testApplication {
            externalServices {
                hosts(testEventHandlerEndpoint) {
                    routing {
                        get("/fetch/innboks/aktive") {
                            call.respondRawJson(aktiveInnboksJson(innboksObject1, innboksObject2))
                        }
                    }
                }
            }
            val innboksConsumer = InnboksConsumer(applicationHttpClient(), URL(testEventHandlerEndpoint))

            runBlocking {
                val externalActiveEvents = innboksConsumer.getExternalActiveEvents(dummyToken)

                externalActiveEvents.size shouldBe 2
                externalActiveEvents shouldContainInnboksObject innboksObject1
                externalActiveEvents shouldContainInnboksObject innboksObject2
            }
        }
    }

    @Test
    fun `should get list of inactive Innboks`() {
        val innboksObject1 = createInnboks("1", "1", false)
        val innboksObject2 = createInnboks("5", "1", false)
        val innboksObject3 = createInnboks("6", "22", false)

        testApplication {
            val innboksConsumer = InnboksConsumer(applicationHttpClient(), URL(testEventHandlerEndpoint))
            externalServices {
                hosts(testEventHandlerEndpoint) {
                    routing {
                        get("/fetch/innboks/inaktive") {
                            call.respondRawJson(inaktiveInnboksJson(innboksObject1, innboksObject2, innboksObject3))
                        }
                    }
                }
            }

            runBlocking {
                val externalInactiveEvents = innboksConsumer.getExternalInactiveEvents(dummyToken)
                externalInactiveEvents shouldContainInnboksObject innboksObject1
                externalInactiveEvents shouldContainInnboksObject innboksObject2
                externalInactiveEvents shouldContainInnboksObject innboksObject3
            }
        }
    }
}

private infix fun List<Innboks>.shouldContainInnboksObject(expectedInnboks: Innboks) =
    find { it.eventId == expectedInnboks.eventId }?.let { event ->
        event.tekst shouldBe expectedInnboks.tekst
        event.fodselsnummer shouldBe expectedInnboks.fodselsnummer
        event.aktiv shouldBe expectedInnboks.aktiv
    } ?: throw AssertionError("Fant ikke innboksvarsel med eventid ${expectedInnboks.eventId}")

private fun aktiveInnboksJson(innboksObject1: Innboks, innboksObject2: Innboks) =
    """[
    ${
        rawEventHandlerVarsel(
            innboksObject1.eventId,
            aktiv = true,
            tekst = innboksObject1.tekst,
            fodselsnummer = innboksObject1.fodselsnummer
        )
    },
    ${
        rawEventHandlerVarsel(
            innboksObject2.eventId,
            aktiv = true,
            tekst = innboksObject2.tekst,
            fodselsnummer = innboksObject2.fodselsnummer
        )
    }]""".trimMargin()

private fun inaktiveInnboksJson(innboksObject1: Innboks, innboksObject2: Innboks, innboksObject3: Innboks) =
    """[
    ${
        rawEventHandlerVarsel(
            innboksObject1.eventId,
            aktiv = false,
            tekst = innboksObject1.tekst,
            fodselsnummer = innboksObject1.fodselsnummer
        )
    },
    ${
        rawEventHandlerVarsel(
            innboksObject2.eventId,
            aktiv = false,
            tekst = innboksObject2.tekst,
            fodselsnummer = innboksObject2.fodselsnummer
        )
    },
    ${
        rawEventHandlerVarsel(
            innboksObject3.eventId,
            aktiv = false,
            tekst = innboksObject3.tekst,
            fodselsnummer = innboksObject3.fodselsnummer
        )
    }]""".trimMargin()