package no.nav.personbruker.dittnav.api.loginstatus

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import no.nav.personbruker.dittnav.api.common.AuthenticatedUserObjectMother.createAuthenticatedUserWithAuxiliaryToken
import org.amshove.kluent.`should be equal to`
import org.junit.jupiter.api.Test

internal class LoginLevelServiceTest {

    private val ident = "123"

    private val innloggingsstatusConsumer: InnloggingsstatusConsumer = mockk()
    private val loginLevelService = LoginLevelService(innloggingsstatusConsumer)

    @Test
    fun `Should return login level accessible directly from oidc token if sufficient`() {
        val currentLoginLevel = 3
        val requiredLoginLevel = 3

        val user = createAuthenticatedUserWithAuxiliaryToken(currentLoginLevel, null)

        val loginLevel = runBlocking {
            loginLevelService.getOperatingLoginLevel(user, requiredLoginLevel)
        }

        loginLevel `should be equal to` currentLoginLevel

        coVerify(exactly = 0) { innloggingsstatusConsumer.fetchAuthSummary(any()) }
    }

    @Test
    fun `Should return login level accessible directly from oidc token if no auxiliary esso token was found, even if insufficient`() {
        val currentLoginLevel = 3
        val requiredLoginLevel = 4

        val user = createAuthenticatedUserWithAuxiliaryToken(currentLoginLevel, null)

        val loginLevel = runBlocking {
            loginLevelService.getOperatingLoginLevel(user, requiredLoginLevel)
        }

        loginLevel `should be equal to` currentLoginLevel

        coVerify(exactly = 0) { innloggingsstatusConsumer.fetchAuthSummary(any()) }
    }

    @Test
    fun `Should fetch login level from innloggingsstatus if current level is insufficient and auxiliary token was found`() {
        val currentLoginLevel = 3
        val requiredLoginLevel = 4

        val auxiliaryToken = "essoToken"

        val user = createAuthenticatedUserWithAuxiliaryToken(currentLoginLevel, auxiliaryToken)

        coEvery { innloggingsstatusConsumer.fetchAuthSummary(user) } returns InnloggingsstatusResponse(true, requiredLoginLevel)

        val loginLevel = runBlocking {
            loginLevelService.getOperatingLoginLevel(user, requiredLoginLevel)
        }

        loginLevel `should be equal to` requiredLoginLevel

        coVerify(exactly = 1) { innloggingsstatusConsumer.fetchAuthSummary(any()) }
    }

    @Test
    fun `Should return login level from oidc token if call to innloggingsstatus failed`() {
        val currentLoginLevel = 3
        val requiredLoginLevel = 4

        val auxiliaryToken = "essoToken"

        val user = createAuthenticatedUserWithAuxiliaryToken(currentLoginLevel, auxiliaryToken)

        coEvery { innloggingsstatusConsumer.fetchAuthSummary(user) } throws Exception()

        val loginLevel = runBlocking {
            loginLevelService.getOperatingLoginLevel(user, requiredLoginLevel)
        }

        loginLevel `should be equal to` currentLoginLevel

        coVerify(exactly = 1) { innloggingsstatusConsumer.fetchAuthSummary(any()) }
    }


}