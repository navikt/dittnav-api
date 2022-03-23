package no.nav.personbruker.dittnav.api.saksoversikt

import io.mockk.*
import kotlinx.coroutines.runBlocking
import no.nav.personbruker.dittnav.api.common.AuthenticatedUserObjectMother
import no.nav.personbruker.dittnav.api.saksoversikt.external.PaabegynteSoknaderExternal
import no.nav.personbruker.dittnav.api.tokenx.AccessToken
import org.amshove.kluent.`should be equal to`
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

internal class SaksoversiktServiceTest {

    private val user = AuthenticatedUserObjectMother.createAuthenticatedUser()
    private val token = AccessToken(user.token)

    private val consumer: SaksoversiktConsumer = mockk()
    private val paabegynteSoknaderTransformer: PaabegynteSoknaderTransformer = mockk()

    private val saksoversiktService = SaksoversiktService(consumer, paabegynteSoknaderTransformer)

    @AfterEach
    fun cleanUp() {
        clearMocks(consumer, paabegynteSoknaderTransformer)
    }

    @Test
    fun `should fetch and transform paabegynte`() {
        val paabegynteSoknader: PaabegynteSoknaderExternal = mockk()

        coEvery {
            consumer.getPaabegynteSoknader(token)
        } returns paabegynteSoknader

        val internal: PaabegynteSoknader = mockk()

        every {
            paabegynteSoknaderTransformer.toInternal(paabegynteSoknader)
        } returns internal

        runBlocking {
            saksoversiktService.getPaabegynte(user)
        }

        coVerify(exactly = 1) { consumer.getPaabegynteSoknader(token) }
        verify(exactly = 1) { paabegynteSoknaderTransformer.toInternal(paabegynteSoknader) }

    }

}
