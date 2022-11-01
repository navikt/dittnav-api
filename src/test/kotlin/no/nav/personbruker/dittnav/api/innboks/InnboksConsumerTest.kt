package no.nav.personbruker.dittnav.api.innboks

import io.kotest.matchers.shouldBe
import io.ktor.server.testing.testApplication
import kotlinx.coroutines.runBlocking
import no.nav.personbruker.dittnav.api.rawEventHandlerVarsel
import no.nav.personbruker.dittnav.api.tokenx.AccessToken
import no.nav.personbruker.dittnav.api.applicationHttpClient
import no.nav.personbruker.dittnav.api.externalServiceWithJsonResponse
import no.nav.personbruker.dittnav.api.toSpesificJsonFormat
import org.junit.jupiter.api.Test
import java.net.URL

internal class InnboksConsumerTest {

    private val dummyToken = AccessToken("<access_token>")
    private val testEventHandlerEndpoint = "http://event-handler"

    @Test
    fun `should get list of active Innboks`() {
        val innboksObject1 = createInnboks(eventId = "1", fodselsnummer = "1", aktiv = true)
        val innboksObject2 = createInnboks(eventId = "2", fodselsnummer = "2", aktiv = true)
        testApplication {

            externalServiceWithJsonResponse(
                hostApiBase = testEventHandlerEndpoint,
                endpoint = "/fetch/innboks/aktive",
                content = listOf(innboksObject1, innboksObject2).toSpesificJsonFormat(Innboks::toEventHandlerJson)
            )
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
        val innboksObject1 = createInnboks(eventId = "1", fodselsnummer = "1", aktiv = false)
        val innboksObject2 = createInnboks(eventId = "5", fodselsnummer = "1", aktiv = false)
        val innboksObject3 = createInnboks(eventId = "6", fodselsnummer = "22", aktiv = false)

        testApplication {
            val innboksConsumer = InnboksConsumer(applicationHttpClient(), URL(testEventHandlerEndpoint))
            externalServiceWithJsonResponse(
                hostApiBase = testEventHandlerEndpoint,
                endpoint = "/fetch/innboks/inaktive",
                content = listOf(
                    innboksObject1,
                    innboksObject2,
                    innboksObject3
                ).toSpesificJsonFormat(Innboks::toEventHandlerJson)
            )

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
