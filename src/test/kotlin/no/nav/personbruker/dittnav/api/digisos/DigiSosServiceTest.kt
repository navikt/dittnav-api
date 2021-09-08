package no.nav.personbruker.dittnav.api.digisos

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import no.nav.personbruker.dittnav.api.beskjed.BeskjedDtoObjectMother
import no.nav.personbruker.dittnav.api.beskjed.KildeType
import no.nav.personbruker.dittnav.api.common.AuthenticatedUserObjectMother
import no.nav.personbruker.dittnav.api.oppgave.OppgaveDtoObjectMother
import org.amshove.kluent.`should contain`
import org.junit.jupiter.api.Test

internal class DigiSosServiceTest {

    private val digiSosConsumer = mockk<DigiSosClient>()
    private val innloggetBruker = AuthenticatedUserObjectMother.createAuthenticatedUser()
    private val digiSosService = DigiSosService(digiSosConsumer)

    @Test
    fun `Skal hente alle paabegynte soknader som er aktive`() {
        coEvery { digiSosConsumer.getPaabegynteActive(any()) } returns listOf(BeskjedDtoObjectMother.createActiveBeskjed("eidAct"))

        val result = runBlocking {
            digiSosService.getPaabegynteActive(innloggetBruker)
        }

        coVerify { digiSosConsumer.getPaabegynteActive(any()) }

        confirmVerified(digiSosConsumer)

        result.successFullSources() `should contain` KildeType.DIGISOS
    }

    @Test
    fun `Skal hente alle paabegynte soknader som er inaktive`() {
        coEvery { digiSosConsumer.getPaabegynteInactive(any()) } returns listOf(BeskjedDtoObjectMother.createInactiveBeskjed("eidInact"))

        val result = runBlocking {
            digiSosService.getPaabegynteInactive(innloggetBruker)
        }

        coVerify { digiSosConsumer.getPaabegynteInactive(any()) }

        confirmVerified(digiSosConsumer)

        result.successFullSources() `should contain` KildeType.DIGISOS
    }

    @Test
    fun `Skal hente alle ettersendelser som er aktive`() {
        coEvery { digiSosConsumer.getEttersendelserActive(any()) } returns listOf(OppgaveDtoObjectMother.createActiveOppgave("eidAct"))

        val result = runBlocking {
            digiSosService.getEttersendelseActive(innloggetBruker)
        }

        coVerify { digiSosConsumer.getEttersendelserActive(any()) }

        confirmVerified(digiSosConsumer)

        result.successFullSources() `should contain` KildeType.DIGISOS
    }

    @Test
    fun `Skal hente alle ettersendelser som er inaktive`() {
        coEvery { digiSosConsumer.getEttersendelserInactive(any()) } returns listOf(OppgaveDtoObjectMother.createInactiveOppgave("eidInact"))

        val result = runBlocking {
            digiSosService.getEttersendelseInactive(innloggetBruker)
        }

        coVerify { digiSosConsumer.getEttersendelserInactive(any()) }

        confirmVerified(digiSosConsumer)

        result.successFullSources() `should contain` KildeType.DIGISOS
    }

    @Test
    fun `Skal haandtere at det skjer feil, og returnere info om hvilken kilde som feilet`() {
        coEvery { digiSosConsumer.getPaabegynteActive(any()) } throws Exception("Simulert feil i en test")

        val result = runBlocking {
            digiSosService.getPaabegynteActive(innloggetBruker)
        }

        result.failedSources() `should contain` KildeType.DIGISOS
    }

}
