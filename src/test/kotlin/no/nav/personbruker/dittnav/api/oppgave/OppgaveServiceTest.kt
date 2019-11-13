package no.nav.personbruker.dittnav.api.oppgave

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.amshove.kluent.*
import org.junit.jupiter.api.Test


class OppgaveServiceTest {

    val oppgaveConsumer = mockk<OppgaveConsumer>()
    val oppgaveService = OppgaveService(oppgaveConsumer)
    val oppgave1 = OppgaveObjectMother.createOppgave("1", "1")
    val oppgave2 = OppgaveObjectMother.createOppgave("2", "2")

    @Test
    fun `should return list of Brukernotifikasjoner when Events are received`() {
        coEvery { oppgaveConsumer.getEvents("1234") } returns listOf(oppgave1, oppgave2)

        runBlocking {
            val brukernotifikasjonListe = oppgaveService.getOppgaveEventsAsBrukernotifikasjoner("1234");

            Verify on oppgaveConsumer that oppgaveConsumer.getEvents("1234") was called
            brukernotifikasjonListe.size `should be equal to` 2
        }

    }

    @Test
    fun `should return empty List when Exception is thrown`() {
        coEvery { oppgaveConsumer.getEvents("1234") } throws Exception("error")

        runBlocking {
            val brukernotifikasjonListe = oppgaveService.getOppgaveEventsAsBrukernotifikasjoner("1234");
            VerifyNotCalled on oppgaveConsumer that oppgaveConsumer.getEvents("1234")
            brukernotifikasjonListe.size `should be equal to` 0
        }

    }

}
