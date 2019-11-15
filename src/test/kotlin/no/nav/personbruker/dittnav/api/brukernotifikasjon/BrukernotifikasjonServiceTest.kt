package no.nav.personbruker.dittnav.api.brukernotifikasjon

import io.mockk.coEvery
import io.mockk.mockk
import no.nav.personbruker.dittnav.api.informasjon.InformasjonService
import no.nav.personbruker.dittnav.api.oppgave.OppgaveService
import org.amshove.kluent.`should be equal to`
import org.junit.jupiter.api.Test

class BrukernotifikasjonServiceTest {
    val oppgaveService = mockk<OppgaveService>()
    val informasjonService = mockk<InformasjonService>()

    val brukernotifikasjonService = BrukernotifikasjonService(oppgaveService, informasjonService);

    val informasjon1 = BrukernotfikasjonObjectMother.createInformasjonsBrukernotifikasjon("1")
    val informasjon2 = BrukernotfikasjonObjectMother.createInformasjonsBrukernotifikasjon("2")
    val oppgave1 = BrukernotfikasjonObjectMother.createOppgaveBrukernotifikasjon("3")

    @Test
    fun `should receive brukernotfikasjoner of type Informasjon and Oppgave`() {
        coEvery { oppgaveService.getOppgaveEventsAsBrukernotifikasjoner("1234") } returns listOf(oppgave1)
        coEvery { informasjonService.getInformasjonEventsAsBrukernotifikasjoner("1234") } returns listOf(informasjon1, informasjon2)

        val brukernotifikasjoner = brukernotifikasjonService.getBrukernotifikasjoner("1234")
        brukernotifikasjoner.size `should be equal to` 3
        brukernotifikasjoner
            .filter { notifikasjon -> notifikasjon.type === BrukernotifikasjonType.OPPGAVE }
            .size `should be equal to` 1
        brukernotifikasjoner
            .filter { notifikasjon -> notifikasjon.type === BrukernotifikasjonType.INFORMASJON }
            .size `should be equal to` 2
    }

    @Test
    fun `should not receieve any brukernotfikasjoner`() {
        coEvery { oppgaveService.getOppgaveEventsAsBrukernotifikasjoner("1234") } returns emptyList()
        coEvery { informasjonService.getInformasjonEventsAsBrukernotifikasjoner("1234") } returns emptyList()

        val brukernotifikasjoner = brukernotifikasjonService.getBrukernotifikasjoner("1234")
        brukernotifikasjoner.size `should be equal to` 0
    }
}
