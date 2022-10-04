package no.nav.personbruker.dittnav.api.oppgave

import io.kotest.matchers.shouldBe
import io.ktor.server.application.call
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import io.ktor.server.testing.testApplication
import kotlinx.coroutines.runBlocking
import no.nav.personbruker.dittnav.api.rawEventHandlerVarsel
import no.nav.personbruker.dittnav.api.respondRawJson
import no.nav.personbruker.dittnav.api.tokenx.AccessToken
import no.nav.personbruker.dittnav.api.util.applicationHttpClient
import org.junit.jupiter.api.Test
import java.net.URL

internal class OppgaveConsumerTest {

    private val dummyToken = AccessToken("<access_token>")
    private val testEventHandlerEndpoint = "https://eventhandler.no"

    @Test
    fun `should get list of aktive oppgavevarsler`() {
        val oppgaveObject1 = createOppgave("1", "1", true)
        val oppgaveObject2 = createOppgave("2", "2", true)

        testApplication {
            val client = applicationHttpClient()
            externalServices {
                hosts(testEventHandlerEndpoint) {
                    routing {
                        get("/fetch/oppgave/aktive") {
                            call.respondRawJson(aktiveOppgaveJson(oppgaveObject1, oppgaveObject2))
                        }
                    }
                }
            }


            val oppgaveConsumer = OppgaveConsumer(client, URL(testEventHandlerEndpoint))

            runBlocking {
                val externalActiveEvents = oppgaveConsumer.getExternalActiveEvents(dummyToken)
                externalActiveEvents shouldContainOppgaveObject oppgaveObject1
                externalActiveEvents shouldContainOppgaveObject oppgaveObject2
            }
        }
    }

    @Test
    fun `should get list of inactive Oppgave`() {
        val oppgaveObject = createOppgave("1", "1", false)
        val oppgaveObject2 = createOppgave("198", "19876413", false)
        val oppgaveObject3 = createOppgave("166", "1961247", false)

        testApplication {
            val client = applicationHttpClient()
            externalServices {
                hosts(testEventHandlerEndpoint) {
                    routing {
                        get("/fetch/oppgave/inaktive") {
                            call.respondRawJson(inaktiveOppgaveJson(oppgaveObject, oppgaveObject2,oppgaveObject3))
                        }
                    }
                }
            }

            val oppgaveConsumer = OppgaveConsumer(client, URL(testEventHandlerEndpoint))

            runBlocking {
                val externalInactiveEvents = oppgaveConsumer.getExternalInactiveEvents(dummyToken)
                externalInactiveEvents.size shouldBe 3
                externalInactiveEvents shouldContainOppgaveObject oppgaveObject
                externalInactiveEvents shouldContainOppgaveObject oppgaveObject2
                externalInactiveEvents shouldContainOppgaveObject oppgaveObject3
            }
        }
    }
}

private infix fun List<Oppgave>.shouldContainOppgaveObject(oppgaveObject: Oppgave) =
    find { it.eventId == oppgaveObject.eventId }?.let { event ->
        event.tekst shouldBe oppgaveObject.tekst
        event.fodselsnummer shouldBe oppgaveObject.fodselsnummer
        event.aktiv shouldBe oppgaveObject.aktiv
    }

private fun aktiveOppgaveJson(oppgaveObjekt1: Oppgave, oppgaveObjekt2: Oppgave) =
    """[
    ${
        rawEventHandlerVarsel(
            oppgaveObjekt1.eventId,
            aktiv = true,
            tekst = oppgaveObjekt1.tekst,
            fodselsnummer = oppgaveObjekt1.fodselsnummer
        )
    },
    ${
        rawEventHandlerVarsel(
            oppgaveObjekt2.eventId,
            aktiv = true,
            tekst = oppgaveObjekt2.tekst,
            fodselsnummer = oppgaveObjekt2.fodselsnummer
        )
    }]""".trimMargin()

private fun aktiveInnboksJson(oppgaveObjekt1: Oppgave, oppgaveObjekt2: Oppgave) =
    """[
    ${
        rawEventHandlerVarsel(
            oppgaveObjekt1.eventId,
            aktiv = true,
            tekst = oppgaveObjekt1.tekst,
            fodselsnummer = oppgaveObjekt1.fodselsnummer
        )
    },
    ${
        rawEventHandlerVarsel(
            oppgaveObjekt2.eventId,
            aktiv = true,
            tekst = oppgaveObjekt2.tekst,
            fodselsnummer = oppgaveObjekt2.fodselsnummer
        )
    }]""".trimMargin()

private fun inaktiveOppgaveJson(oppgaveObject1: Oppgave, oppgaveObject2: Oppgave, oppgaveObject3: Oppgave) =
    """[
    ${
        rawEventHandlerVarsel(
            oppgaveObject1.eventId,
            aktiv = false,
            tekst = oppgaveObject1.tekst,
            fodselsnummer = oppgaveObject1.fodselsnummer
        )
    },
    ${
        rawEventHandlerVarsel(
            oppgaveObject2.eventId,
            aktiv = false,
            tekst = oppgaveObject2.tekst,
            fodselsnummer = oppgaveObject2.fodselsnummer
        )
    },
    ${
        rawEventHandlerVarsel(
            oppgaveObject3.eventId,
            aktiv = false,
            tekst = oppgaveObject3.tekst,
            fodselsnummer = oppgaveObject3.fodselsnummer
        )
    }]""".trimMargin()