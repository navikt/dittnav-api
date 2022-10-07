package no.nav.personbruker.dittnav.api.oppgave

import io.kotest.matchers.shouldBe
import io.ktor.server.testing.testApplication
import kotlinx.coroutines.runBlocking
import no.nav.personbruker.dittnav.api.applicationHttpClient
import no.nav.personbruker.dittnav.api.rawEventHandlerVarsel
import no.nav.personbruker.dittnav.api.setupExternalServiceWithJsonResponse
import no.nav.personbruker.dittnav.api.toSpesificJsonFormat
import no.nav.personbruker.dittnav.api.tokenx.AccessToken
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
            setupExternalServiceWithJsonResponse(
                hostApiBase = testEventHandlerEndpoint,
                endpoint = "/fetch/oppgave/aktive",
                content = listOf(oppgaveObject1, oppgaveObject2).toSpesificJsonFormat(Oppgave::toEventHandlerJson)
            )

            val oppgaveConsumer = OppgaveConsumer(applicationHttpClient(), URL(testEventHandlerEndpoint))

            runBlocking {
                val externalActiveEvents = oppgaveConsumer.getExternalActiveEvents(dummyToken)
                externalActiveEvents shouldContainOppgaveObject oppgaveObject1
                externalActiveEvents shouldContainOppgaveObject oppgaveObject2
            }
        }
    }

    @Test
    fun `should get list of inactive oppgavevarsler`() {
        val oppgaveObject = createOppgave("1", "1", false)
        val oppgaveObject2 = createOppgave("198", "19876413", false)
        val oppgaveObject3 = createOppgave("166", "1961247", false)

        testApplication {

            setupExternalServiceWithJsonResponse(
                hostApiBase = testEventHandlerEndpoint,
                endpoint = "/fetch/oppgave/inaktive",
                content = listOf(
                    oppgaveObject,
                    oppgaveObject2,
                    oppgaveObject3
                ).toSpesificJsonFormat(Oppgave::toEventHandlerJson)
            )

            val oppgaveConsumer = OppgaveConsumer(applicationHttpClient(), URL(testEventHandlerEndpoint))

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

private fun Oppgave.toEventHandlerJson(): String = rawEventHandlerVarsel(
    eventId = eventId,
    fodselsnummer = fodselsnummer,
    grupperingsId = grupperingsId,
    førstBehandlet = "$forstBehandlet",
    produsent = produsent,
    sikkerhetsnivå = 0,
    sistOppdatert = "$sistOppdatert",
    tekst = tekst,
    link = link,
    eksternVarslingSendt = eksternVarslingSendt,
    eksternVarslingKanaler = eksternVarslingKanaler,
    aktiv = aktiv
)

private infix fun List<Oppgave>.shouldContainOppgaveObject(expected: Oppgave) =
    find { it.eventId == expected.eventId }?.let { event ->
        event.tekst shouldBe expected.tekst
        event.fodselsnummer shouldBe expected.fodselsnummer
        event.aktiv shouldBe expected.aktiv
    }