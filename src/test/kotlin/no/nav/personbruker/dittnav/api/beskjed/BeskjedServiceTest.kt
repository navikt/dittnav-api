package no.nav.personbruker.dittnav.api.beskjed

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import no.nav.personbruker.dittnav.api.common.InnloggetBrukerObjectMother
import org.amshove.kluent.`should be equal to`
import org.junit.jupiter.api.Test


class BeskjedServiceTest {

    val innloggetBruker = InnloggetBrukerObjectMother.createInnloggetBruker()

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

}
