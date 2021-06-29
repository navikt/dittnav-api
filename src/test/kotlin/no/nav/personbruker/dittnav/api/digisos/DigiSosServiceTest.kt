package no.nav.personbruker.dittnav.api.digisos

import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import no.nav.personbruker.dittnav.api.common.AuthenticatedUserObjectMother
import org.junit.jupiter.api.Test

internal class DigiSosServiceTest {

    private val digiSosConsumer = mockk<DigiSosConsumer>()
    private val innloggetBruker = AuthenticatedUserObjectMother.createAuthenticatedUser()
    private val digiSosService = DigiSosService(digiSosConsumer)

    @Test
    fun `Skal hente alle paabegynte soknader som er aktive`() {
        runBlocking {
            digiSosService.getPaabegynteActive(innloggetBruker)
        }

        coVerify { digiSosConsumer.getPaabegynteActive(any()) }

        confirmVerified(digiSosConsumer)
    }

    @Test
    fun `Skal hente alle paabegynte soknader som er inaktive`() {
        runBlocking {
            digiSosService.getPaabegynteInactive(innloggetBruker)
        }

        coVerify { digiSosConsumer.getPaabegynteInactive(any()) }

        confirmVerified(digiSosConsumer)
    }

}
