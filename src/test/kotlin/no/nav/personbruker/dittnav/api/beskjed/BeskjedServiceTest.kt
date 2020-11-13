package no.nav.personbruker.dittnav.api.beskjed

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import no.nav.personbruker.dittnav.api.common.AuthenticatedUserObjectMother
import no.nav.personbruker.dittnav.api.loginstatus.LoginLevelService
import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.`should contain`
import org.junit.jupiter.api.Test

class BeskjedServiceTest {

    var user = AuthenticatedUserObjectMother.createAuthenticatedUser()

    val beskjedConsumer = mockk<BeskjedConsumer>()
    val loginLevelService = mockk<LoginLevelService>()
    val beskjedService = BeskjedService(beskjedConsumer, loginLevelService)

    @Test
    fun `should return list of BeskjedDTO when active Events are received`() {
        val beskjed1 = createBeskjed("1", "1", "1", true)
        val beskjed2 = createBeskjed("2", "2", "2", true)
        coEvery { beskjedConsumer.getExternalActiveEvents(user) } returns listOf(beskjed1, beskjed2)
        coEvery { loginLevelService.getOperatingLoginLevel(any(), any()) } returns user.loginLevel
        runBlocking {
            val beskjedResult = beskjedService.getActiveBeskjedEvents(user)
            beskjedResult.results().size `should be equal to` 2
        }
    }

    @Test
    fun `should return list of BeskjedDTO when inactive Events are received`() {
        val beskjed1 = createBeskjed("1", "1", "1", false)
        val beskjed2 = createBeskjed("2", "2", "2", false)
        coEvery { beskjedConsumer.getExternalInactiveEvents(user) } returns listOf(beskjed1, beskjed2)
        coEvery { loginLevelService.getOperatingLoginLevel(any(), any()) } returns user.loginLevel
        runBlocking {
            val beskjedResult = beskjedService.getInactiveBeskjedEvents(user)
            beskjedResult.results().size `should be equal to` 2
        }
    }

    @Test
    fun `should mask events with security level higher than current user`() {
        val ident = "1"
        var beskjed = createBeskjed("1", ident, "1", true)
        beskjed = beskjed.copy(sikkerhetsnivaa = 4)
        user = AuthenticatedUserObjectMother.createAuthenticatedUser(ident, 3)
        coEvery { beskjedConsumer.getExternalActiveEvents(user) } returns listOf(beskjed)
        coEvery { loginLevelService.getOperatingLoginLevel(any(), any()) } returns user.loginLevel
        runBlocking {
            val beskjedResult = beskjedService.getActiveBeskjedEvents(user)
            val beskjedDTO = beskjedResult.results().first()
            beskjedDTO.tekst `should be equal to` "***"
            beskjedDTO.link `should be equal to` "***"
            beskjedDTO.sikkerhetsnivaa `should be equal to` 4
        }
    }

    @Test
    fun `should not mask events with security level lower than current user`() {
        var beskjed = createBeskjed("1", "1", "1", true)
        beskjed = beskjed.copy(sikkerhetsnivaa = 3)
        coEvery { beskjedConsumer.getExternalActiveEvents(user) } returns listOf(beskjed)
        coEvery { loginLevelService.getOperatingLoginLevel(any(), any()) } returns user.loginLevel
        runBlocking {
            val beskjedResult = beskjedService.getActiveBeskjedEvents(user)
            val beskjedDTO = beskjedResult.results().first()
            beskjedDTO.tekst `should be equal to` beskjed.tekst
            beskjedDTO.link `should be equal to` beskjed.link
            beskjedDTO.sikkerhetsnivaa `should be equal to` 3
        }
    }

    @Test
    fun `should not mask events with security level equal than current user`() {
        val beskjed = createBeskjed("1", "1", "1", true)
        coEvery { beskjedConsumer.getExternalActiveEvents(user) } returns listOf(beskjed)
        coEvery { loginLevelService.getOperatingLoginLevel(any(), any()) } returns user.loginLevel
        runBlocking {
            val beskjedResult = beskjedService.getActiveBeskjedEvents(user)
            val beskjedDTO = beskjedResult.results().first()
            beskjedDTO.tekst `should be equal to` beskjed.tekst
            beskjedDTO.link `should be equal to` beskjed.link
            beskjedDTO.sikkerhetsnivaa `should be equal to` 4
        }
    }

    @Test
    fun `should throw exception if fetching active events fails`() {
        coEvery { beskjedConsumer.getExternalActiveEvents(user) } throws Exception("error")
        coEvery { loginLevelService.getOperatingLoginLevel(any(), any()) } returns user.loginLevel
        runBlocking {
            val beskjedResult = beskjedService.getActiveBeskjedEvents(user)
            beskjedResult.hasErrors() `should be equal to` true
            beskjedResult.errors() `should contain` KildeType.EVENTHANDLER
        }
    }

    @Test
    fun `should throw exception if fetching inactive events fails`() {
        coEvery { beskjedConsumer.getExternalInactiveEvents(user) } throws Exception("error")
        coEvery { loginLevelService.getOperatingLoginLevel(any(), any()) } returns user.loginLevel
        runBlocking {
            val beskjedResult = beskjedService.getInactiveBeskjedEvents(user)
            beskjedResult.hasErrors() `should be equal to` true
            beskjedResult.errors() `should contain` KildeType.EVENTHANDLER
        }
    }
}
