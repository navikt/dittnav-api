package no.nav.personbruker.dittnav.api.brukernotifikasjon

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import no.nav.personbruker.dittnav.api.beskjed.BeskjedService
import no.nav.personbruker.dittnav.api.common.InnloggetBrukerObjectMother
import no.nav.personbruker.dittnav.api.innboks.InnboksService
import no.nav.personbruker.dittnav.api.oppgave.OppgaveService
import org.amshove.kluent.`should be equal to`
import org.junit.jupiter.api.Test

class BrukernotifikasjonServiceTest {
    val oppgaveService = mockk<OppgaveService>()
    val beskjedService = mockk<BeskjedService>()
    val innboksService = mockk<InnboksService>()

    val brukernotifikasjonService = BrukernotifikasjonService(oppgaveService, beskjedService, innboksService);

    val beskjed1 = BrukernotfikasjonObjectMother.createBeskjedsBrukernotifikasjon("1", "1")
    val beskjed2 = BrukernotfikasjonObjectMother.createBeskjedsBrukernotifikasjon("2", "2")
    val oppgave1 = BrukernotfikasjonObjectMother.createOppgaveBrukernotifikasjon("3")
    val innboks1 = BrukernotfikasjonObjectMother.createInnboksBrukernotifikasjon("4")

    val innloggetBruker = InnloggetBrukerObjectMother.createInnloggetBruker()

    @Test
    fun `should receive brukernotfikasjoner of type Beskjed and Oppgave`() {
        coEvery { oppgaveService.getOppgaveEventsAsBrukernotifikasjoner(innloggetBruker) } returns listOf(oppgave1)
        coEvery { beskjedService.getBeskjedEventsAsBrukernotifikasjoner(innloggetBruker) } returns listOf(beskjed1, beskjed2)
        coEvery { innboksService.getInnboksEventsAsBrukernotifikasjoner(innloggetBruker) } returns listOf(innboks1)

        val brukernotifikasjoner = runBlocking { brukernotifikasjonService.getBrukernotifikasjoner(innloggetBruker) }
        brukernotifikasjoner.size `should be equal to` 4
        brukernotifikasjoner
            .filter { notifikasjon -> notifikasjon.type === BrukernotifikasjonType.OPPGAVE }
            .size `should be equal to` 1
        brukernotifikasjoner
            .filter { notifikasjon -> notifikasjon.type === BrukernotifikasjonType.BESKJED }
            .size `should be equal to` 2
        brukernotifikasjoner
            .filter { notifikasjon -> notifikasjon.type === BrukernotifikasjonType.INNBOKS }
            .size `should be equal to` 1
    }

    @Test
    fun `should not receieve any brukernotfikasjoner`() {
        coEvery { oppgaveService.getOppgaveEventsAsBrukernotifikasjoner(innloggetBruker) } returns emptyList()
        coEvery { beskjedService.getBeskjedEventsAsBrukernotifikasjoner(innloggetBruker) } returns emptyList()
        coEvery { innboksService.getInnboksEventsAsBrukernotifikasjoner(innloggetBruker) } returns emptyList()

        val brukernotifikasjoner = runBlocking { brukernotifikasjonService.getBrukernotifikasjoner(innloggetBruker) }
        brukernotifikasjoner.size `should be equal to` 0
    }
}
