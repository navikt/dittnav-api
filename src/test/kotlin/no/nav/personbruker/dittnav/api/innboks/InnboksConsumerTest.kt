package no.nav.personbruker.dittnav.api.innboks

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
import no.nav.personbruker.dittnav.api.TestUser
import no.nav.personbruker.dittnav.api.applicationHttpClient
import no.nav.personbruker.dittnav.api.assert
import no.nav.personbruker.dittnav.api.config.ConsumeEventException
import no.nav.personbruker.dittnav.api.externalServiceWithJsonResponse
import no.nav.personbruker.dittnav.api.toSpesificJsonFormat
import no.nav.personbruker.dittnav.api.tokenx.EventhandlerTokendings
import org.junit.jupiter.api.Test
import java.net.URL

internal class InnboksConsumerTest {


    private val dummyUser = TestUser.createAuthenticatedUser()
    private val testEventHandlerURL = "http://event-handler"
    private val eventhandlerTokendings = mockk<EventhandlerTokendings>().also {
        coEvery { it.exchangeToken(any()) } returns "<access-token>"
    }

    @Test
    fun `should get list of active Innboks`() {
        val innboksObject1 = createInnboks(eventId = "1", fodselsnummer = "1", aktiv = true)
        val innboksObject2 = createInnboks(eventId = "2", fodselsnummer = "2", aktiv = true)
        testApplication {

            externalServiceWithJsonResponse(
                hostApiBase = testEventHandlerURL,
                endpoint = "/fetch/innboks/aktive",
                content = listOf(innboksObject1, innboksObject2).toSpesificJsonFormat(Innboks::toEventHandlerJson)
            )

            InnboksConsumer(applicationHttpClient(), eventhandlerTokendings, URL(testEventHandlerURL))
                .getActiveInnboksEvents(dummyUser)
                .assert {
                    size shouldBe 2
                    this shouldContainInnboksObject innboksObject1
                    this shouldContainInnboksObject innboksObject2
                }
        }
    }

    @Test
    fun `should get list of inactive Innboks`() {
        val innboksObject1 = createInnboks(eventId = "1", fodselsnummer = "1", aktiv = false)
        val innboksObject2 = createInnboks(eventId = "5", fodselsnummer = "1", aktiv = false)
        val innboksObject3 = createInnboks(eventId = "6", fodselsnummer = "22", aktiv = false)

        testApplication {
            externalServiceWithJsonResponse(
                hostApiBase = testEventHandlerURL,
                endpoint = "/fetch/innboks/inaktive",
                content = listOf(
                    innboksObject1,
                    innboksObject2,
                    innboksObject3
                ).toSpesificJsonFormat(Innboks::toEventHandlerJson)
            )

            InnboksConsumer(applicationHttpClient(), eventhandlerTokendings, URL(testEventHandlerURL))
                .getInactiveInnboksEvents(dummyUser)
                .assert {
                    this shouldContainInnboksObject innboksObject1
                    this shouldContainInnboksObject innboksObject2
                    this shouldContainInnboksObject innboksObject3
                }
        }
    }

    @Test
    fun `should throw exception if fetching events fails`() =
        testApplication {
            val innboksConsumer =
                InnboksConsumer(applicationHttpClient(), eventhandlerTokendings, URL(testEventHandlerURL))
            externalServices {
                hosts(testEventHandlerURL) {
                    routing {
                        get("fetch/innboks/inaktive") {
                            call.respond(HttpStatusCode.InternalServerError)
                        }
                        get("fetch/innboks/aktive") {
                            call.respond(HttpStatusCode.InternalServerError)
                        }
                    }
                }
            }
            shouldThrow<ConsumeEventException> { innboksConsumer.getActiveInnboksEvents(dummyUser) }
            shouldThrow<ConsumeEventException> { innboksConsumer.getInactiveInnboksEvents(dummyUser) }
        }

}

private infix fun List<InnboksDTO>.shouldContainInnboksObject(expectedInnboks: Innboks) =
    find { it.eventId == expectedInnboks.eventId }?.let { event ->
        event.tekst shouldBe expectedInnboks.tekst
        event.produsent shouldBe event.produsent
    } ?: throw AssertionError("Fant ikke innboksvarsel med eventid ${expectedInnboks.eventId}")
