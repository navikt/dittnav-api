package no.nav.personbruker.dittnav.api.innboks

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import no.nav.personbruker.dittnav.api.common.InnloggetBrukerObjectMother
import no.nav.personbruker.dittnav.api.common.SecurityLevel
import org.amshove.kluent.`should be equal to`
import org.junit.jupiter.api.Test

class InnboksServiceTest {
    val innboksConsumer = mockk<InnboksConsumer>()
    val innboksService = InnboksService(innboksConsumer)

    var innloggetBruker = InnloggetBrukerObjectMother.createInnloggetBruker(SecurityLevel.Level4)

    @Test
    fun `should return list of InnboksDTO when active Events are received`() {
        val innboks1 = createInnboks("1", "1", true)
        val innboks2 = createInnboks("2", "2", true)
        coEvery { innboksConsumer.getExternalActiveEvents(innloggetBruker) } returns listOf(innboks1, innboks2)
        runBlocking {
            val brukernotifikasjonListe = innboksService.getActiveInnboksEvents(innloggetBruker)
            brukernotifikasjonListe.size `should be equal to` 2
        }
    }

    @Test
    fun `should return list of InnboksDTO when inactive Events are received`() {
        val innboks1 = createInnboks("1", "1", false)
        val innboks2 = createInnboks("2", "2", false)
        coEvery { innboksConsumer.getExternalInactiveEvents(innloggetBruker) } returns listOf(innboks1, innboks2)
        runBlocking {
            val brukernotifikasjonListe = innboksService.getInactiveInnboksEvents(innloggetBruker)
            brukernotifikasjonListe.size `should be equal to` 2
        }
    }

    @Test
    fun `should return empty list when Exception is thrown`() {
        coEvery { innboksConsumer.getExternalActiveEvents(innloggetBruker) } throws Exception("error")

        runBlocking {
            val brukernotifikasjonListe = innboksService.getActiveInnboksEvents(innloggetBruker)
            brukernotifikasjonListe.size `should be equal to` 0
        }
    }

    @Test
    fun `should mask events with security level higher than current user`() {
        var innboks = createInnboks("1", "1", true)
        innboks = innboks.copy(sikkerhetsnivaa = 4)
        innloggetBruker = InnloggetBrukerObjectMother.createInnloggetBruker(SecurityLevel.Level3)
        coEvery { innboksConsumer.getExternalActiveEvents(innloggetBruker) } returns listOf(innboks)
        runBlocking {
            val innboksList = innboksService.getActiveInnboksEvents(innloggetBruker)
            val innboksDTO = innboksList.first()
            innboksDTO.tekst `should be equal to` "***"
            innboksDTO.link `should be equal to` "***"
            innboksDTO.sikkerhetsnivaa `should be equal to` 4
        }
    }

    @Test
    fun `should not mask events with security level lower than current user`() {
        var innboks = createInnboks("1", "1", true)
        innboks = innboks.copy(sikkerhetsnivaa = 3)
        coEvery { innboksConsumer.getExternalActiveEvents(innloggetBruker) } returns listOf(innboks)
        runBlocking {
            val innboksList = innboksService.getActiveInnboksEvents(innloggetBruker)
            val innboksDTO = innboksList.first()
            innboksDTO.tekst `should be equal to` innboks.tekst
            innboksDTO.link `should be equal to` innboks.link
            innboksDTO.sikkerhetsnivaa `should be equal to` 3
        }
    }

    @Test
    fun `should not mask events with security level equal than current user`() {
        var innboks = createInnboks("1", "1", true)
        coEvery { innboksConsumer.getExternalActiveEvents(innloggetBruker) } returns listOf(innboks)
        runBlocking {
            val innboksList = innboksService.getActiveInnboksEvents(innloggetBruker)
            val innboksDTO = innboksList.first()
            innboksDTO.tekst `should be equal to` innboks.tekst
            innboksDTO.link `should be equal to` innboks.link
            innboksDTO.sikkerhetsnivaa `should be equal to` 4
        }
    }
}
