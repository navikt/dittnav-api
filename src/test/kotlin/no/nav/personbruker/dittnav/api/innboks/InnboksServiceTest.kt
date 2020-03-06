package no.nav.personbruker.dittnav.api.innboks

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import no.nav.personbruker.dittnav.api.common.InnloggetBrukerObjectMother
import org.amshove.kluent.`should be equal to`
import org.junit.jupiter.api.Test

class InnboksServiceTest {
    val innboksConsumer = mockk<InnboksConsumer>()
    val innboksService = InnboksService(innboksConsumer)
    val innboks1 = createInnboks("1", "1")
    val innboks2 = createInnboks("2", "2")
    val innloggetBruker = InnloggetBrukerObjectMother.createInnloggetBruker()

    @Test
    fun `should return list of Brukernotifikasjoner when Events are received`() {
        coEvery { innboksConsumer.getExternalActiveEvents(innloggetBruker) } returns listOf(innboks1, innboks2)

        runBlocking {
            val brukernotifikasjonListe = innboksService.getActiveInnboksEvents(innloggetBruker)
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
}
