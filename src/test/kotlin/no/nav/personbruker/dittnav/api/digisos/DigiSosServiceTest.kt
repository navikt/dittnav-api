package no.nav.personbruker.dittnav.api.digisos

import io.kotest.matchers.collections.shouldContain
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import no.nav.personbruker.dittnav.api.beskjed.BeskjedDTO
import no.nav.personbruker.dittnav.api.beskjed.KildeType
import no.nav.personbruker.dittnav.api.beskjed.createActiveBeskjedDto
import no.nav.personbruker.dittnav.api.authentication.AuthenticatedUserTestData
import no.nav.personbruker.dittnav.api.beskjed.createInactiveBeskjed
import org.junit.jupiter.api.Test
import java.time.ZonedDateTime

internal class DigiSosServiceTest {

    private val digiSosConsumer = mockk<DigiSosClient>()
    private val innloggetBruker = AuthenticatedUserTestData.createAuthenticatedUser()
    private val digiSosService = DigiSosService(digiSosConsumer)

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
        coEvery { digiSosConsumer.getPaabegynteInactive(any()) } returns listOf(createInactiveBeskjed("eidInact"))

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