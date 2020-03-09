package no.nav.personbruker.dittnav.api.beskjed

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import no.nav.personbruker.dittnav.api.common.InnloggetBrukerObjectMother
import no.nav.personbruker.dittnav.api.common.SecurityLevel
import org.amshove.kluent.`should be equal to`
import org.junit.jupiter.api.Test


class BeskjedServiceTest {

    var innloggetBruker = InnloggetBrukerObjectMother.createInnloggetBruker(SecurityLevel.Level4)

    val beskjedConsumer = mockk<BeskjedConsumer>()
    val beskjedService = BeskjedService(beskjedConsumer)

    @Test
    fun `should return list of BeskjedDTO when active Events are received`() {
        var beskjed1 = createBeskjed("1", "1", "1", true)
        var beskjed2 = createBeskjed("2", "2", "2", true)
        coEvery { beskjedConsumer.getExternalActiveEvents(innloggetBruker) } returns listOf(beskjed1, beskjed2)
        runBlocking {
            val brukernotifikasjonListe = beskjedService.getActiveBeskjedEvents(innloggetBruker)
            brukernotifikasjonListe.size `should be equal to` 2
        }
    }

    @Test
    fun `should return list of BeskjedDTO when inactive Events are received`() {
        var beskjed1 = createBeskjed("1", "1", "1", false)
        var beskjed2 = createBeskjed("2", "2", "2", false)
        coEvery { beskjedConsumer.getExternalInactiveEvents(innloggetBruker) } returns listOf(beskjed1, beskjed2)
        runBlocking {
            val brukernotifikasjonListe = beskjedService.getInactiveBeskjedEvents(innloggetBruker)
            brukernotifikasjonListe.size `should be equal to` 2
        }
    }

    @Test
    fun `should return empty list when Exception is thrown`() {
        coEvery { beskjedConsumer.getExternalActiveEvents(innloggetBruker) } throws Exception("error")

        runBlocking {
            val brukernotifikasjonListe = beskjedService.getActiveBeskjedEvents(innloggetBruker)
            brukernotifikasjonListe.size `should be equal to` 0
        }
    }

    @Test
    fun `should mask events with security level higher than current user`() {
        var beskjed = createBeskjed("1", "1", "1", true)
        beskjed = beskjed.copy(sikkerhetsnivaa = 4)
        innloggetBruker = InnloggetBrukerObjectMother.createInnloggetBruker(SecurityLevel.Level3)
        coEvery { beskjedConsumer.getExternalActiveEvents(innloggetBruker) } returns listOf(beskjed)
        runBlocking {
            val beskjedList = beskjedService.getActiveBeskjedEvents(innloggetBruker)
            val beskjedDTO = beskjedList.first()
            beskjedDTO.tekst `should be equal to` "***"
            beskjedDTO.link `should be equal to` "***"
            beskjedDTO.sikkerhetsnivaa `should be equal to` 4
        }
    }

    @Test
    fun `should not mask events with security level lower than current user`() {
        var beskjed = createBeskjed("1", "1", "1", true)
        beskjed = beskjed.copy(sikkerhetsnivaa = 3)
        coEvery { beskjedConsumer.getExternalActiveEvents(innloggetBruker) } returns listOf(beskjed)
        runBlocking {
            val beskjedList = beskjedService.getActiveBeskjedEvents(innloggetBruker)
            val beskjedDTO = beskjedList.first()
            beskjedDTO.tekst `should be equal to` beskjed.tekst
            beskjedDTO.link `should be equal to` beskjed.link
            beskjedDTO.sikkerhetsnivaa `should be equal to` 3
        }
    }

    @Test
    fun `should not mask events with security level equal than current user`() {
        var beskjed = createBeskjed("1", "1", "1", true)
        coEvery { beskjedConsumer.getExternalActiveEvents(innloggetBruker) } returns listOf(beskjed)
        runBlocking {
            val beskjedList = beskjedService.getActiveBeskjedEvents(innloggetBruker)
            val beskjedDTO = beskjedList.first()
            beskjedDTO.tekst `should be equal to` beskjed.tekst
            beskjedDTO.link `should be equal to` beskjed.link
            beskjedDTO.sikkerhetsnivaa `should be equal to` 4
        }
    }
}
