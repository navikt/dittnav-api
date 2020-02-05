package no.nav.personbruker.dittnav.api.oppgave

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import no.nav.personbruker.dittnav.api.common.createInnloggetBruker
import org.amshove.kluent.`should be equal to`
import org.junit.jupiter.api.Test


class OppgaveServiceTest {

    val oppgaveConsumer = mockk<OppgaveConsumer>()
    val oppgaveService = OppgaveService(oppgaveConsumer)
    val oppgave1 = OppgaveObjectMother.createOppgave("1", "1")
    val oppgave2 = OppgaveObjectMother.createOppgave("2", "2")
    val innloggetBruker = createInnloggetBruker()

    @Test
    fun `should return list of Brukernotifikasjoner when Events are received`() {
        coEvery { oppgaveConsumer.getExternalEvents(innloggetBruker) } returns listOf(oppgave1, oppgave2)

        runBlocking {
            val brukernotifikasjonListe = oppgaveService.getOppgaveEventsAsBrukernotifikasjoner(innloggetBruker)
            brukernotifikasjonListe.size `should be equal to` 2
        }

    }

    @Test
    fun `should return empty List when Exception is thrown`() {
        coEvery { oppgaveConsumer.getExternalEvents(innloggetBruker) } throws Exception("error")

        runBlocking {
            val brukernotifikasjonListe = oppgaveService.getOppgaveEventsAsBrukernotifikasjoner(innloggetBruker)
            brukernotifikasjonListe.size `should be equal to` 0
        }

    }

}
