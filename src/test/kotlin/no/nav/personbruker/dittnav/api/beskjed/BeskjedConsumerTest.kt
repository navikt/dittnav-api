package no.nav.personbruker.dittnav.api.beskjed

import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.shouldBe
import io.ktor.server.testing.testApplication
import io.mockk.coEvery
import io.mockk.mockk
import no.nav.personbruker.dittnav.api.TestUser
import no.nav.personbruker.dittnav.api.applicationHttpClient
import no.nav.personbruker.dittnav.api.assert
import no.nav.personbruker.dittnav.api.createBeskjed
import no.nav.personbruker.dittnav.api.externalServiceWith500Response
import no.nav.personbruker.dittnav.api.rawEventHandlerVarsel
import no.nav.personbruker.dittnav.api.externalServiceWithJsonResponse
import no.nav.personbruker.dittnav.api.toSpesificJsonFormat
import no.nav.personbruker.dittnav.api.tokenx.EventhandlerTokendings
import org.junit.jupiter.api.Test
import java.net.URL
import kotlin.AssertionError

internal class BeskjedConsumerTest {

    private val testEventHandlerUrl = "https://test.eventhandler.no"
    private val dummyUser = TestUser.createAuthenticatedUser()
    private val eventhandlerTokendings = mockk<EventhandlerTokendings>().also {
        coEvery { it.exchangeToken(any()) } returns "tokensmoken"
    }


    @Test
    fun `Skal hente en liste over aktive Beskjeder`() {
        val beskjedOject = createBeskjed(eventId = "12345", fodselsnummer = "9876543210", aktiv = true)

        testApplication {
            externalServiceWithJsonResponse(
                hostApiBase = testEventHandlerUrl,
                endpoint = "fetch/beskjed/aktive",
                content = listOf(beskjedOject).toSpesificJsonFormat(Beskjed::toRawEventhandlerVarsel)
            )

            BeskjedConsumer(applicationHttpClient(), eventhandlerTokendings, URL(testEventHandlerUrl))
                .getActiveBeskjedEvents(dummyUser)
                .assert {
                    val result = results()
                    result.size shouldBe 1
                    result shouldContainBeskjedObject beskjedOject
                    failedSources().size shouldBe 0
                    successFullSources().size shouldBe 1
                    successFullSources().first() shouldBe KildeType.EVENTHANDLER
                }
        }
    }

    @Test
    fun `Skal hente en liste over inaktive Beskjeder`() {
        val beskjedObject = createBeskjed(eventId = "1", fodselsnummer = "1", aktiv = false)
        val beskjedObject2 = createBeskjed(eventId = "1", fodselsnummer = "1", aktiv = false)

        testApplication {
            externalServiceWithJsonResponse(
                hostApiBase = testEventHandlerUrl,
                endpoint = "fetch/beskjed/inaktive",
                content = listOf(beskjedObject, beskjedObject2).toSpesificJsonFormat(Beskjed::toRawEventhandlerVarsel)
            )


            BeskjedConsumer(applicationHttpClient(), eventhandlerTokendings, URL(testEventHandlerUrl))
                .getInactiveBeskjedEvents(dummyUser)
                .assert {
                    val result = results()
                    result.size shouldBe 2
                    result shouldContainBeskjedObject beskjedObject2
                    result shouldContainBeskjedObject beskjedObject
                    failedSources().size shouldBe 0
                    successFullSources().size shouldBe 1
                    successFullSources().first() shouldBe KildeType.EVENTHANDLER

                }
        }
    }

    @Test
    fun `should throw exception if fetching active events fails`() = testApplication {

        externalServiceWith500Response(testEventHandlerUrl, "fetch/beskjed/aktive")

        BeskjedConsumer(applicationHttpClient(), eventhandlerTokendings, URL(testEventHandlerUrl))
            .getActiveBeskjedEvents(dummyUser)
            .assert {
                hasErrors() shouldBe true
                failedSources() shouldContain KildeType.EVENTHANDLER
            }
    }


    @Test
    fun `should throw exception if fetching inactive events fails`() = testApplication {

        externalServiceWith500Response(testEventHandlerUrl, "fetch/beskjed/aktive")

        BeskjedConsumer(applicationHttpClient(), eventhandlerTokendings, URL(testEventHandlerUrl))
            .getActiveBeskjedEvents(dummyUser)
            .assert {
                hasErrors() shouldBe true
                failedSources() shouldContain KildeType.EVENTHANDLER
            }
    }
}

private infix fun List<BeskjedDTO>.shouldContainBeskjedObject(expected: Beskjed) =
    find { it.eventId == expected.eventId }?.let { result ->
        result.tekst shouldBe expected.tekst
        result.aktiv shouldBe expected.aktiv
        result.eksternVarslingSendt shouldBe expected.eksternVarslingSendt
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
