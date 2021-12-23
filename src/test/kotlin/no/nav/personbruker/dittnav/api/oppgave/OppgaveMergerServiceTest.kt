package no.nav.personbruker.dittnav.api.oppgave

import io.mockk.*
import kotlinx.coroutines.runBlocking
import no.finn.unleash.FakeUnleash
import no.nav.personbruker.dittnav.api.beskjed.KildeType
import no.nav.personbruker.dittnav.api.common.AuthenticatedUserObjectMother
import no.nav.personbruker.dittnav.api.common.MultiSourceResultObjectMother
import no.nav.personbruker.dittnav.api.digisos.DigiSosService
import no.nav.personbruker.dittnav.api.unleash.UnleashService
import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.`should contain all`
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class OppgaveMergerServiceTest {

    private val oppgaveService = mockk<OppgaveService>(relaxed = true)
    private val digiSosService = mockk<DigiSosService>(relaxed = true)

    private val innloggetBruker = AuthenticatedUserObjectMother.createAuthenticatedUser()

    private val eventHandlerDefaultResult = MultiSourceResultObjectMother.giveMeNumberOfSuccessfulOppgaveEventsForSource(
        1,
        KildeType.EVENTHANDLER,
        "handler"
    )

    private val digiSosDefaultResult = MultiSourceResultObjectMother.giveMeNumberOfSuccessfulOppgaveEventsForSource(
        3,
        KildeType.DIGISOS,
        "digiSos"
    )

    @BeforeEach
    fun setup() {
        clearMocks(oppgaveService, digiSosService)

        coEvery { oppgaveService.getActiveOppgaveEvents(any()) } returns eventHandlerDefaultResult
        coEvery { oppgaveService.getInactiveOppgaveEvents(any()) } returns eventHandlerDefaultResult

        coEvery { digiSosService.getEttersendelseActive(any()) } returns digiSosDefaultResult
        coEvery { digiSosService.getEttersendelseInactive(any()) } returns digiSosDefaultResult
    }

    @Test
    fun `Hent aktive alltid fra event-handler`() {
        val fakeUnleash = FakeUnleash()
        val unleashService = UnleashService(fakeUnleash)

        val oppgaveMerger = OppgaveMergerService(oppgaveService, digiSosService, unleashService)

        val result = runBlocking {
            oppgaveMerger.getActiveEvents(innloggetBruker)
        }

        coVerify(exactly = 1) { oppgaveService.getActiveOppgaveEvents(any()) }
        coVerify(exactly = 0) { digiSosService.getEttersendelseActive(any()) }

        confirmVerified(oppgaveService)
        confirmVerified(digiSosService)

        result.successFullSources().size `should be equal to` 1
        result.successFullSources() `should contain all` listOf(KildeType.EVENTHANDLER)
    }

    @Test
    fun `Hent ettersendelser fra DigiSos hvis aktivert`() {
        val fakeUnleash = FakeUnleash().apply {
            enable(UnleashService.digiSosOppgaveToggleName)
        }
        val unleashService = UnleashService(fakeUnleash)

        val oppgaveMerger = OppgaveMergerService(oppgaveService, digiSosService, unleashService)

        val result = runBlocking {
            oppgaveMerger.getActiveEvents(innloggetBruker)
        }

        coVerify(exactly = 1) { oppgaveService.getActiveOppgaveEvents(any()) }
        coVerify(exactly = 1) { digiSosService.getEttersendelseActive(any()) }

        confirmVerified(oppgaveService)
        confirmVerified(digiSosService)

        result.successFullSources().size `should be equal to` 2
        result.successFullSources() `should contain all` listOf(KildeType.EVENTHANDLER, KildeType.DIGISOS)
    }

    @Test
    fun `Hent aktive fra alle kilder hvis aktivert`() {
        val fakeUnleash = FakeUnleash().apply {
            enable(UnleashService.varselinnboksToggleName)
            enable(UnleashService.digiSosOppgaveToggleName)
        }
        val unleashService = UnleashService(fakeUnleash)

        val oppgaveMerger = OppgaveMergerService(oppgaveService, digiSosService, unleashService)

        val result = runBlocking {
            oppgaveMerger.getActiveEvents(innloggetBruker)
        }

        coVerify(exactly = 1) { oppgaveService.getActiveOppgaveEvents(any()) }
        coVerify(exactly = 1) { digiSosService.getEttersendelseActive(any()) }

        confirmVerified(oppgaveService)
        confirmVerified(digiSosService)

        result.successFullSources().size `should be equal to` 2
        result.successFullSources() `should contain all` listOf(KildeType.EVENTHANDLER, KildeType.DIGISOS)
    }

    @Test
    fun `Hent inaktive alltid fra event-handler`() {
        val fakeUnleash = FakeUnleash()
        val unleashService = UnleashService(fakeUnleash)

        val oppgaveMerger = OppgaveMergerService(oppgaveService, digiSosService, unleashService)

        val result = runBlocking {
            oppgaveMerger.getInactiveEvents(innloggetBruker)
        }

        coVerify(exactly = 1) { oppgaveService.getInactiveOppgaveEvents(any()) }
        coVerify(exactly = 0) { digiSosService.getEttersendelseInactive(any()) }

        confirmVerified(oppgaveService)
        confirmVerified(digiSosService)

        result.successFullSources().size `should be equal to` 1
        result.successFullSources() `should contain all` listOf(KildeType.EVENTHANDLER)
    }

    @Test
    fun `Hent inaktive fra DigiSos hvis aktivert`() {
        val fakeUnleash = FakeUnleash().apply {
            enable(UnleashService.digiSosOppgaveToggleName)
        }
        val unleashService = UnleashService(fakeUnleash)

        val oppgaveMerger = OppgaveMergerService(oppgaveService, digiSosService, unleashService)

        val result = runBlocking {
            oppgaveMerger.getInactiveEvents(innloggetBruker)
        }

        coVerify(exactly = 1) { oppgaveService.getInactiveOppgaveEvents(any()) }
        coVerify(exactly = 1) { digiSosService.getEttersendelseInactive(any()) }

        confirmVerified(oppgaveService)
        confirmVerified(digiSosService)

        result.successFullSources().size `should be equal to` 2
        result.successFullSources() `should contain all` listOf(KildeType.EVENTHANDLER, KildeType.DIGISOS)
    }

    @Test
    fun `Hent inaktive fra alle kilder hvis aktivert`() {
        val fakeUnleash = FakeUnleash().apply {
            enable(UnleashService.varselinnboksToggleName)
            enable(UnleashService.digiSosOppgaveToggleName)
        }
        val unleashService = UnleashService(fakeUnleash)
        val oppgaveMerger = OppgaveMergerService(oppgaveService, digiSosService, unleashService)

        val result = runBlocking {
            oppgaveMerger.getInactiveEvents(innloggetBruker)
        }

        coVerify(exactly = 1) { oppgaveService.getInactiveOppgaveEvents(any()) }
        coVerify(exactly = 1) { digiSosService.getEttersendelseInactive(any()) }

        confirmVerified(oppgaveService)
        confirmVerified(digiSosService)

        result.successFullSources().size `should be equal to` 2
        result.successFullSources() `should contain all` listOf(KildeType.EVENTHANDLER, KildeType.DIGISOS)
    }

}
