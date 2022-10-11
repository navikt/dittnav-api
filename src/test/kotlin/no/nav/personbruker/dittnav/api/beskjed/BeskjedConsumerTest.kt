package no.nav.personbruker.dittnav.api.beskjed

import io.kotest.matchers.shouldBe
import io.ktor.server.testing.testApplication
import kotlinx.coroutines.runBlocking
import no.nav.personbruker.dittnav.api.applicationHttpClient
import no.nav.personbruker.dittnav.api.rawEventHandlerVarsel
import no.nav.personbruker.dittnav.api.externalServiceWithJsonResponse
import no.nav.personbruker.dittnav.api.toSpesificJsonFormat
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

        testApplication {
            externalServiceWithJsonResponse(
                hostApiBase = testEventHandlerUrl,
                endpoint = "fetch/beskjed/aktive",
                content = listOf(beskjedOject).toSpesificJsonFormat(Beskjed::toRawEventhandlerVarsel)
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

        testApplication {
            externalServiceWithJsonResponse(
                hostApiBase = testEventHandlerUrl,
                endpoint = "fetch/beskjed/inaktive",
                content = listOf(beskjedObject, beskjedObject2).toSpesificJsonFormat(Beskjed::toRawEventhandlerVarsel)
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

private fun Beskjed.toRawEventhandlerVarsel(): String = rawEventHandlerVarsel(
    førstBehandlet = "$forstBehandlet",
    fodselsnummer = fodselsnummer,
    eventId = eventId,
    grupperingsId = grupperingsId,
    tekst = tekst,
    link = link,
    produsent = produsent,
    sikkerhetsnivå = sikkerhetsnivaa,
    sistOppdatert = "$sistOppdatert",
    aktiv = aktiv,
    eksternVarslingSendt = eksternVarslingSendt,
    eksternVarslingKanaler = eksternVarslingKanaler
)