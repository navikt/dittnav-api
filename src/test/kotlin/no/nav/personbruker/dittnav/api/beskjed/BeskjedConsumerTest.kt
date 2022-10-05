package no.nav.personbruker.dittnav.api.beskjed

import io.kotest.matchers.shouldBe
import io.ktor.server.testing.testApplication
import kotlinx.coroutines.runBlocking
import no.nav.personbruker.dittnav.api.applicationHttpClient
import no.nav.personbruker.dittnav.api.rawEventHandlerVarsel
import no.nav.personbruker.dittnav.api.setupExternalServiceWithJsonResponse
import no.nav.personbruker.dittnav.api.tokenx.AccessToken
import org.junit.jupiter.api.Test
import java.net.URL
import kotlin.AssertionError

internal class BeskjedConsumerTest {

    private val testEventHandlerUrl = "https://test.eventhandler.no"
    private val dummyToken = AccessToken("<access_token>")

    @Test
    fun `Skal motta en liste over aktive Beskjeder`() {
        val beskjedOject = createBeskjed(eventId = "12345", fodselsnummer = "9876543210", aktiv = true)
        val eventhandlerResponse = "[${
            rawEventHandlerVarsel(
                fodselsnummer = beskjedOject.fodselsnummer,
                tekst = beskjedOject.tekst,
                aktiv = true
            )
        }]"
        testApplication {

            setupExternalServiceWithJsonResponse(
                hostApiBase = testEventHandlerUrl,
                endpoint = "fetch/beskjed/aktive",
                content = eventhandlerResponse
            )

            val beskjedConsumer = BeskjedConsumer(applicationHttpClient(), URL(testEventHandlerUrl))

            runBlocking {
                val externalActiveEvents = beskjedConsumer.getExternalActiveEvents(dummyToken)
                externalActiveEvents.size shouldBe 1
                externalActiveEvents shouldContainBeskjedObject beskjedOject
                externalActiveEvents.size shouldBe 1
            }
        }
    }

    @Test
    fun `Skal motta en liste over inaktive Beskjeder`() {
        val beskjedObject = createBeskjed(eventId = "1", fodselsnummer = "1", aktiv = false)
        val beskjedObject2 = createBeskjed(eventId = "1", fodselsnummer = "1", aktiv = false)
        val eventhandlerResponse = "[${
            rawEventHandlerVarsel(
                fodselsnummer = beskjedObject.fodselsnummer,
                eventId = beskjedObject.eventId,
                tekst = beskjedObject.tekst,
                aktiv = false
            )
        },${
            rawEventHandlerVarsel(
                fodselsnummer = beskjedObject2.fodselsnummer,
                eventId = beskjedObject2.eventId,
                tekst = beskjedObject2.tekst,
                aktiv = false
            )
        }]"


        testApplication {
            setupExternalServiceWithJsonResponse(
                hostApiBase = testEventHandlerUrl,
                endpoint = "fetch/beskjed/inaktive",
                content = eventhandlerResponse
            )

            val beskjedConsumer = BeskjedConsumer(applicationHttpClient(), URL(testEventHandlerUrl))

            runBlocking {
                val externalInactiveEvents = beskjedConsumer.getExternalInactiveEvents(dummyToken)
                externalInactiveEvents.size shouldBe 2
                externalInactiveEvents shouldContainBeskjedObject beskjedObject
                externalInactiveEvents shouldContainBeskjedObject beskjedObject2
            }
        }
    }

}

private infix fun List<Beskjed>.shouldContainBeskjedObject(expected: Beskjed) =
    find { it.eventId == expected.eventId }?.let { event ->
        event.tekst shouldBe expected.tekst
        event.fodselsnummer shouldBe expected.fodselsnummer
        event.aktiv shouldBe expected.aktiv
    } ?: throw AssertionError("Fant ikke beskjed med eventId ${expected.eventId}")

