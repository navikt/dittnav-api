package no.nav.personbruker.dittnav.api.oppgave

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import io.ktor.server.testing.testApplication
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import no.nav.personbruker.dittnav.api.TestUser
import no.nav.personbruker.dittnav.api.applicationHttpClient
import no.nav.personbruker.dittnav.api.config.ConsumeEventException
import no.nav.personbruker.dittnav.api.rawEventHandlerVarsel
import no.nav.personbruker.dittnav.api.externalServiceWithJsonResponse
import no.nav.personbruker.dittnav.api.toSpesificJsonFormat
import no.nav.personbruker.dittnav.api.tokenx.AccessToken
import no.nav.personbruker.dittnav.api.tokenx.EventhandlerTokendings
import org.junit.jupiter.api.Test

import java.net.URL

internal class OppgaveConsumerTest {

    private val dummyUser = TestUser.createAuthenticatedUser()
    private val testEventHandlerURL = "https://eventhandler.no"
    private val mockkTokendings = mockk<EventhandlerTokendings>().also {
        coEvery { it.exchangeToken(any()) } returns AccessToken("Access!")
    }

    @Test
    fun `should get list of aktive oppgavevarsler`() {
        val oppgaveObject1 = createOppgave(eventId = "1", fødselsnummer = "1", aktiv=true)
        val oppgaveObject2 = createOppgave(eventId = "2", fødselsnummer = "2", aktiv=true)

        testApplication {
            externalServiceWithJsonResponse(
                hostApiBase = testEventHandlerURL,
                endpoint = "/fetch/oppgave/aktive",
                content = listOf(oppgaveObject1, oppgaveObject2).toSpesificJsonFormat(Oppgave::toEventHandlerJson)
            )

            val oppgaveConsumer = OppgaveConsumer(applicationHttpClient(), mockkTokendings,URL(testEventHandlerURL))

            runBlocking {
                val externalActiveEvents = oppgaveConsumer.getActiveOppgaver(dummyUser)
                externalActiveEvents shouldContainOppgaveObject oppgaveObject1
                externalActiveEvents shouldContainOppgaveObject oppgaveObject2
            }
        }
    }

    @Test
    fun `should get list of inactive oppgavevarsler`() {
        val oppgaveObject = createOppgave(eventId = "1", fødselsnummer = "1", aktiv = false)
        val oppgaveObject2 = createOppgave(eventId = "198", fødselsnummer = "19876413", aktiv = false)
        val oppgaveObject3 = createOppgave(eventId = "166", fødselsnummer = "1961247", aktiv = false)

        testApplication {

            externalServiceWithJsonResponse(
                hostApiBase = testEventHandlerURL,
                endpoint = "/fetch/oppgave/inaktive",
                content = listOf(
                    oppgaveObject,
                    oppgaveObject2,
                    oppgaveObject3
                ).toSpesificJsonFormat(Oppgave::toEventHandlerJson)
            )

            val oppgaveConsumer = OppgaveConsumer(applicationHttpClient(), mockkTokendings,URL(testEventHandlerURL))

            runBlocking {
                val externalInactiveEvents = oppgaveConsumer.getInactiveOppgaver(dummyUser)
                externalInactiveEvents.size shouldBe 3
                externalInactiveEvents shouldContainOppgaveObject oppgaveObject
                externalInactiveEvents shouldContainOppgaveObject oppgaveObject2
                externalInactiveEvents shouldContainOppgaveObject oppgaveObject3
            }
        }
    }

    @Test
    fun `should throw exception if fetching events fails`() =
        testApplication {
            val oppgaveConsumer =
                OppgaveConsumer(applicationHttpClient(), mockkTokendings, URL(testEventHandlerURL))
            externalServices {
                hosts(testEventHandlerURL) {
                    routing {
                        get("fetch/oppgave/inaktive") {
                            call.respond(HttpStatusCode.InternalServerError)
                        }
                        get("fetch/oppgave/aktive") {
                            call.respond(HttpStatusCode.InternalServerError)
                        }
                    }
                }
            }
            shouldThrow<ConsumeEventException> { oppgaveConsumer.getActiveOppgaver(dummyUser) }
            shouldThrow<ConsumeEventException> { oppgaveConsumer.getInactiveOppgaver(dummyUser) }
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

private infix fun List<OppgaveDTO>.shouldContainOppgaveObject(expected: Oppgave) =
    find { it.eventId == expected.eventId }?.let { event ->
        event.tekst shouldBe expected.tekst
        event.aktiv shouldBe expected.aktiv
    }