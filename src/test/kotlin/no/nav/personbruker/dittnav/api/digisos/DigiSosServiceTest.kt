package no.nav.personbruker.dittnav.api.digisos

import io.kotest.matchers.collections.shouldContain
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import no.nav.personbruker.dittnav.api.beskjed.KildeType
import no.nav.personbruker.dittnav.api.beskjed.createActiveBeskjedDto
import no.nav.personbruker.dittnav.api.authentication.AuthenticatedUserTestData
import no.nav.personbruker.dittnav.api.beskjed.createInactiveBeskjedDto
import no.nav.personbruker.dittnav.api.tokenx.AccessToken
import org.junit.jupiter.api.Test

internal class DigiSosServiceTest {

    private val digiSosConsumer = mockk<DigiSosConsumer>()
    private val digiSosTokendings = mockk<DigiSosTokendings>().also {
        coEvery { it.exchangeToken(any()) } returns AccessToken("Access!")
    }
    private val innloggetBruker = AuthenticatedUserTestData.createAuthenticatedUser()
    private val digiSosService = DigiSosService(digiSosConsumer, digiSosTokendings)

    @Test
    fun `Skal hente alle paabegynte soknader som er aktive`() {
        coEvery { digiSosConsumer.getPaabegynteActive(any()) } returns listOf(
            createActiveBeskjedDto(
                "eidAct"
            )
        )

        val result = runBlocking {
            digiSosService.getPaabegynteActive(innloggetBruker)
        }

        coVerify { digiSosConsumer.getPaabegynteActive(any()) }

        confirmVerified(digiSosConsumer)

        result.successFullSources() shouldContain KildeType.DIGISOS
    }

    @Test
    fun `Skal hente alle paabegynte soknader som er inaktive`() {
        coEvery { digiSosConsumer.getPaabegynteInactive(any()) } returns listOf(createInactiveBeskjedDto("eidInact"))

        val result = runBlocking {
            digiSosService.getPaabegynteInactive(innloggetBruker)
        }

        coVerify { digiSosConsumer.getPaabegynteInactive(any()) }

        confirmVerified(digiSosConsumer)

        result.successFullSources() shouldContain KildeType.DIGISOS
    }

    @Test
    fun `Skal haandtere at det skjer feil, og returnere info om hvilken kilde som feilet`() {
        coEvery { digiSosConsumer.getPaabegynteActive(any()) } throws Exception("Simulert feil i en test")

        val result = runBlocking {
            digiSosService.getPaabegynteActive(innloggetBruker)
        }

        result.failedSources() shouldContain KildeType.DIGISOS
    }

}
