package no.nav.personbruker.dittnav.api.innboks;

import io.ktor.client.request.get
import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.server.testing.testApplication
import io.mockk.coEvery
import io.mockk.mockk
import no.nav.personbruker.dittnav.api.applicationHttpClient
import no.nav.personbruker.dittnav.api.mockApi
import no.nav.personbruker.dittnav.api.rawEventHandlerVarsel
import no.nav.personbruker.dittnav.api.tokenx.AccessToken
import no.nav.personbruker.dittnav.api.tokenx.EventhandlerTokendings
import org.junit.jupiter.api.Test
import java.net.URL


class InnboksApiTest {
    private val eventhandlerTestHost = "https://eventhandler.test"

    @Test
    fun testGetInnboks() = testApplication {
        mockApi(innboksService = createInnboksService())
        client.get("/innboks").apply {
        //    TODO("Please write your test here")
        }
    }

    @Test
    fun testGetInnboksInaktiv() = testApplication {
        mockApi(innboksService = createInnboksService())
        client.get("/innboks/inaktiv").apply {
        //    TODO("Please write your test here")
        }
    }

    private fun ApplicationTestBuilder.createInnboksService(): InnboksService = InnboksService(
        innboksConsumer = InnboksConsumer(
            client = applicationHttpClient(),
            eventHandlerBaseURL = URL(eventhandlerTestHost)
        ),
        eventhandlerTokenDings = mockk<EventhandlerTokendings>().also {
            coEvery { it.exchangeToken(any()) } returns AccessToken("duumyToken")
        }
    )

}


private fun InnboksDTO.toEventhandlerJson() = rawEventHandlerVarsel(
    eventId = eventId,
    førstBehandlet = "$forstBehandlet",
    produsent = produsent,
    sikkerhetsnivå = 0,
    sistOppdatert = "$sistOppdatert",
    tekst = tekst,
    link = link,
    eksternVarslingSendt = eksternVarslingSendt,
    eksternVarslingKanaler = eksternVarslingKanaler, fodselsnummer = "27468263", grupperingsId = "", aktiv = true
)


