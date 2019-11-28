package no.nav.personbruker.dittnav.api.brukernotifikasjon

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import no.nav.personbruker.dittnav.api.informasjon.InformasjonService
import no.nav.personbruker.dittnav.api.innboks.InnboksService
import no.nav.personbruker.dittnav.api.oppgave.OppgaveService
import org.amshove.kluent.`should be equal to`
import org.junit.jupiter.api.Test

class BrukernotifikasjonServiceTest {
    val oppgaveService = mockk<OppgaveService>()
    val informasjonService = mockk<InformasjonService>()
    val innboksService = mockk<InnboksService>()

    val brukernotifikasjonService = BrukernotifikasjonService(oppgaveService, informasjonService, innboksService);

    val informasjon1 = BrukernotfikasjonObjectMother.createInformasjonsBrukernotifikasjon("1")
    val informasjon2 = BrukernotfikasjonObjectMother.createInformasjonsBrukernotifikasjon("2")
    val oppgave1 = BrukernotfikasjonObjectMother.createOppgaveBrukernotifikasjon("3")
    val innboks1 = BrukernotfikasjonObjectMother.createInnboksBrukernotifikasjon("4")

    @Test
    fun `should receive brukernotfikasjoner of type Informasjon and Oppgave`() {
        coEvery { oppgaveService.getOppgaveEventsAsBrukernotifikasjoner("1234") } returns listOf(oppgave1)
        coEvery { informasjonService.getInformasjonEventsAsBrukernotifikasjoner("1234") } returns listOf(informasjon1, informasjon2)
        coEvery { innboksService.getInnboksEventsAsBrukernotifikasjoner("1234") } returns listOf(innboks1)

        val brukernotifikasjoner = runBlocking { brukernotifikasjonService.getBrukernotifikasjoner("1234") }
        brukernotifikasjoner.size `should be equal to` 4
        brukernotifikasjoner
            .filter { notifikasjon -> notifikasjon.type === BrukernotifikasjonType.OPPGAVE }
            .size `should be equal to` 1
        brukernotifikasjoner
            .filter { notifikasjon -> notifikasjon.type === BrukernotifikasjonType.INFORMASJON }
            .size `should be equal to` 2
        brukernotifikasjoner
            .filter { notifikasjon -> notifikasjon.type === BrukernotifikasjonType.INNBOKS }
            .size `should be equal to` 1
    }

    @Test
    fun `should not receieve any brukernotfikasjoner`() {
        coEvery { oppgaveService.getOppgaveEventsAsBrukernotifikasjoner("1234") } returns emptyList()
        coEvery { informasjonService.getInformasjonEventsAsBrukernotifikasjoner("1234") } returns emptyList()
        coEvery { innboksService.getInnboksEventsAsBrukernotifikasjoner("1234") } returns emptyList()

        val brukernotifikasjoner = runBlocking { brukernotifikasjonService.getBrukernotifikasjoner("1234") }
        brukernotifikasjoner.size `should be equal to` 0
    }
}
