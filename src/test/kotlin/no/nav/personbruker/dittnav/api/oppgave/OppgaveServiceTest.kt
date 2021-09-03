package no.nav.personbruker.dittnav.api.oppgave

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import no.nav.personbruker.dittnav.api.beskjed.KildeType
import no.nav.personbruker.dittnav.api.common.AuthenticatedUserObjectMother
import no.nav.personbruker.dittnav.api.loginstatus.LoginLevelService
import org.amshove.kluent.`should be equal to`
import org.junit.jupiter.api.Test

class OppgaveServiceTest {

    val oppgaveConsumer = mockk<OppgaveConsumer>()
    val loginLevelService = mockk<LoginLevelService>()
    val oppgaveService = OppgaveService(oppgaveConsumer, loginLevelService)
    var user = AuthenticatedUserObjectMother.createAuthenticatedUser()

    @Test
    fun `should return list of OppgaveDTO when active Events are received`() {
        val oppgave1 = createOppgave("1", "1", true)
        val oppgave2 = createOppgave("2", "2", true)
        coEvery { oppgaveConsumer.getExternalActiveEvents(user) } returns listOf(oppgave1, oppgave2)
        coEvery { loginLevelService.getOperatingLoginLevel(any(), any()) } returns user.loginLevel
        runBlocking {
            val oppgaveList = oppgaveService.getActiveOppgaveEvents(user)
            oppgaveList.results().size `should be equal to` 2
        }
    }

    @Test
    fun `should return list of OppgaveDTO when inactive Events are received`() {
        val oppgave1 = createOppgave("1", "1", false)
        val oppgave2 = createOppgave("2", "2", false)
        coEvery { oppgaveConsumer.getExternalInactiveEvents(user) } returns listOf(oppgave1, oppgave2)
        coEvery { loginLevelService.getOperatingLoginLevel(any(), any()) } returns user.loginLevel
        runBlocking {
            val oppgaveList = oppgaveService.getInactiveOppgaveEvents(user)
            oppgaveList.results().size `should be equal to` 2
        }
    }

    @Test
    fun `should mask events with security level higher than current user`() {
        val ident = "1"
        var oppgave = createOppgave("1", ident, true)
        oppgave = oppgave.copy(sikkerhetsnivaa = 4)
        user = AuthenticatedUserObjectMother.createAuthenticatedUser(ident, 3)
        coEvery { oppgaveConsumer.getExternalActiveEvents(user) } returns listOf(oppgave)
        coEvery { loginLevelService.getOperatingLoginLevel(any(), any()) } returns user.loginLevel
        runBlocking {
            val oppgaveList = oppgaveService.getActiveOppgaveEvents(user)
            val oppgaveDTO = oppgaveList.results().first()
            oppgaveDTO.tekst `should be equal to` "***"
            oppgaveDTO.link `should be equal to` "***"
            oppgaveDTO.sikkerhetsnivaa `should be equal to` 4
        }
    }

    @Test
    fun `should not mask events with security level lower than current user`() {
        var oppgave = createOppgave("1", "1", true)
        oppgave = oppgave.copy(sikkerhetsnivaa = 3)
        coEvery { oppgaveConsumer.getExternalActiveEvents(user) } returns listOf(oppgave)
        coEvery { loginLevelService.getOperatingLoginLevel(any(), any()) } returns user.loginLevel
        runBlocking {
            val oppgaveList = oppgaveService.getActiveOppgaveEvents(user)
            val oppgaveDTO = oppgaveList.results().first()
            oppgaveDTO.tekst `should be equal to` oppgave.tekst
            oppgaveDTO.link `should be equal to` oppgave.link
            oppgaveDTO.sikkerhetsnivaa `should be equal to` 3
        }
    }

    @Test
    fun `should not mask events with security level equal than current user`() {
        val oppgave = createOppgave("1", "1", true)
        coEvery { oppgaveConsumer.getExternalActiveEvents(user) } returns listOf(oppgave)
        coEvery { loginLevelService.getOperatingLoginLevel(any(), any()) } returns user.loginLevel
        runBlocking {
            val oppgaveList = oppgaveService.getActiveOppgaveEvents(user)
            val oppgaveDTO = oppgaveList.results().first()
            oppgaveDTO.tekst `should be equal to` oppgave.tekst
            oppgaveDTO.link `should be equal to` oppgave.link
            oppgaveDTO.sikkerhetsnivaa `should be equal to` 4
        }
    }

    @Test
    fun `should return error-result if fetching active events fails`() {
        coEvery { oppgaveConsumer.getExternalActiveEvents(user) } throws Exception("error")
        coEvery { loginLevelService.getOperatingLoginLevel(any(), any()) } returns user.loginLevel

        val result = runBlocking {
            oppgaveService.getActiveOppgaveEvents(user)
        }

        result.hasErrors() `should be equal to` true
        result.failedSources()[0] `should be equal to` KildeType.EVENTHANDLER
    }

    @Test
    fun `should return error-result if fetching inactive events fails`() {
        coEvery { oppgaveConsumer.getExternalInactiveEvents(user) } throws Exception("error")
        coEvery { loginLevelService.getOperatingLoginLevel(any(), any()) } returns user.loginLevel

        val result = runBlocking {
            oppgaveService.getInactiveOppgaveEvents(user)
        }

        result.hasErrors() `should be equal to` true
        result.failedSources()[0] `should be equal to` KildeType.EVENTHANDLER
    }

}
