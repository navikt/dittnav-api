package no.nav.personbruker.dittnav.api.oppgave

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import no.nav.personbruker.dittnav.api.common.InnloggetBrukerObjectMother
import org.amshove.kluent.`should be equal to`
import org.junit.jupiter.api.Test

class OppgaveServiceTest {

    val oppgaveConsumer = mockk<OppgaveConsumer>()
    val oppgaveService = OppgaveService(oppgaveConsumer)
    var innloggetBruker = InnloggetBrukerObjectMother.createInnloggetBruker()

    @Test
    fun `should return list of OppgaveDTO when active Events are received`() {
        val oppgave1 = createOppgave("1", "1", true)
        val oppgave2 = createOppgave("2", "2", true)
        coEvery { oppgaveConsumer.getExternalActiveEvents(innloggetBruker) } returns listOf(oppgave1, oppgave2)
        runBlocking {
            val brukernotifikasjonListe = oppgaveService.getActiveOppgaveEvents(innloggetBruker)
            brukernotifikasjonListe.size `should be equal to` 2
        }
    }

    @Test
    fun `should return list of OppgaveDTO when inactive Events are received`() {
        val oppgave1 = createOppgave("1", "1", false)
        val oppgave2 = createOppgave("2", "2", false)
        coEvery { oppgaveConsumer.getExternalInactiveEvents(innloggetBruker) } returns listOf(oppgave1, oppgave2)
        runBlocking {
            val brukernotifikasjonListe = oppgaveService.getInactiveOppgaveEvents(innloggetBruker)
            brukernotifikasjonListe.size `should be equal to` 2
        }
    }

    @Test
    fun `should return empty List when Exception is thrown`() {
        coEvery { oppgaveConsumer.getExternalActiveEvents(innloggetBruker) } throws Exception("error")

        runBlocking {
            val brukernotifikasjonListe = oppgaveService.getActiveOppgaveEvents(innloggetBruker)
            brukernotifikasjonListe.size `should be equal to` 0
        }
    }

    @Test
    fun `should mask events with security level higher than current user`() {
        val ident = "1"
        var oppgave = createOppgave("1", ident, true)
        oppgave = oppgave.copy(sikkerhetsnivaa = 4)
        innloggetBruker = InnloggetBrukerObjectMother.createInnloggetBruker(ident, 3)
        coEvery { oppgaveConsumer.getExternalActiveEvents(innloggetBruker) } returns listOf(oppgave)
        runBlocking {
            val oppgaveList = oppgaveService.getActiveOppgaveEvents(innloggetBruker)
            val oppgaveDTO = oppgaveList.first()
            oppgaveDTO.tekst `should be equal to` "***"
            oppgaveDTO.link `should be equal to` "***"
            oppgaveDTO.sikkerhetsnivaa `should be equal to` 4
        }
    }

    @Test
    fun `should not mask events with security level lower than current user`() {
        var oppgave = createOppgave("1", "1", true)
        oppgave = oppgave.copy(sikkerhetsnivaa = 3)
        coEvery { oppgaveConsumer.getExternalActiveEvents(innloggetBruker) } returns listOf(oppgave)
        runBlocking {
            val oppgaveList = oppgaveService.getActiveOppgaveEvents(innloggetBruker)
            val oppgaveDTO = oppgaveList.first()
            oppgaveDTO.tekst `should be equal to` oppgave.tekst
            oppgaveDTO.link `should be equal to` oppgave.link
            oppgaveDTO.sikkerhetsnivaa `should be equal to` 3
        }
    }

    @Test
    fun `should not mask events with security level equal than current user`() {
        val oppgave = createOppgave("1", "1", true)
        coEvery { oppgaveConsumer.getExternalActiveEvents(innloggetBruker) } returns listOf(oppgave)
        runBlocking {
            val oppgaveList = oppgaveService.getActiveOppgaveEvents(innloggetBruker)
            val oppgaveDTO = oppgaveList.first()
            oppgaveDTO.tekst `should be equal to` oppgave.tekst
            oppgaveDTO.link `should be equal to` oppgave.link
            oppgaveDTO.sikkerhetsnivaa `should be equal to` 4
        }
    }
}
