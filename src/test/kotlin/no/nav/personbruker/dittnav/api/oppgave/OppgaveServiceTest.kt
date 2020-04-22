package no.nav.personbruker.dittnav.api.oppgave

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import no.nav.personbruker.dittnav.api.common.ConsumeEventException
import no.nav.personbruker.dittnav.api.common.InnloggetBrukerObjectMother
import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.`should throw`
import org.amshove.kluent.invoking
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
            val oppgaveList = oppgaveService.getActiveOppgaveEvents(innloggetBruker)
            oppgaveList.size `should be equal to` 2
        }
    }

    @Test
    fun `should return list of OppgaveDTO when inactive Events are received`() {
        val oppgave1 = createOppgave("1", "1", false)
        val oppgave2 = createOppgave("2", "2", false)
        coEvery { oppgaveConsumer.getExternalInactiveEvents(innloggetBruker) } returns listOf(oppgave1, oppgave2)
        runBlocking {
            val oppgaveList = oppgaveService.getInactiveOppgaveEvents(innloggetBruker)
            oppgaveList.size `should be equal to` 2
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

    @Test
    fun `should throw exception if fetching active events fails`() {
        coEvery { oppgaveConsumer.getExternalActiveEvents(innloggetBruker) } throws Exception("error")
        invoking { runBlocking { oppgaveService.getActiveOppgaveEvents(innloggetBruker) } } `should throw` ConsumeEventException::class
    }

    @Test
    fun `should throw exception if fetching inactive events fails`() {
        coEvery { oppgaveConsumer.getExternalInactiveEvents(innloggetBruker) } throws Exception("error")
        invoking { runBlocking { oppgaveService.getInactiveOppgaveEvents(innloggetBruker) } } `should throw` ConsumeEventException::class
    }
}
