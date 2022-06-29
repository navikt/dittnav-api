package no.nav.personbruker.dittnav.api.beskjed

import io.kotest.matchers.shouldBe
import io.mockk.*
import kotlinx.coroutines.runBlocking
import no.finn.unleash.FakeUnleash
import no.nav.personbruker.dittnav.api.common.AuthenticatedUserObjectMother
import no.nav.personbruker.dittnav.api.common.MultiSourceResultObjectMother
import no.nav.personbruker.dittnav.api.digisos.DigiSosService
import no.nav.personbruker.dittnav.api.unleash.UnleashService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class BeskjedMergerServiceTest {

    private val beskjedService = mockk<BeskjedService>(relaxed = true)
    private val digiSosService = mockk<DigiSosService>(relaxed = true)

    private val innloggetBruker = AuthenticatedUserObjectMother.createAuthenticatedUser()

    private val eventHandlerDefaultResult = MultiSourceResultObjectMother.giveMeNumberOfSuccessfulBeskjedEventsForSource(
        1,
        KildeType.EVENTHANDLER,
        "handler"
    )

    private val digiSosDefaultResult = MultiSourceResultObjectMother.giveMeNumberOfSuccessfulBeskjedEventsForSource(
        3,
        KildeType.DIGISOS,
        "digiSos"
    )

    @BeforeEach
    fun setup() {
        clearMocks(beskjedService, digiSosService)

        coEvery { beskjedService.getActiveBeskjedEvents(any()) } returns eventHandlerDefaultResult
        coEvery { beskjedService.getInactiveBeskjedEvents(any()) } returns eventHandlerDefaultResult

        coEvery { digiSosService.getPaabegynteActive(any()) } returns digiSosDefaultResult
        coEvery { digiSosService.getPaabegynteInactive(any()) } returns digiSosDefaultResult
    }

    @Test
    fun `Hent aktive alltid fra event-handler`() {
        val fakeUnleash = FakeUnleash()
        val unleashService = UnleashService(fakeUnleash)

        val beskjedMerger = BeskjedMergerService(beskjedService, digiSosService, unleashService)

        val result = runBlocking {
            beskjedMerger.getActiveEvents(innloggetBruker)
        }

        coVerify(exactly = 1) { beskjedService.getActiveBeskjedEvents(any()) }
        coVerify(exactly = 0) { digiSosService.getPaabegynteActive(any()) }

        confirmVerified(beskjedService)
        confirmVerified(digiSosService)

        result.successFullSources().size shouldBe 1
        result.successFullSources() shouldBe listOf(KildeType.EVENTHANDLER)
    }

    @Test
    fun `Hent påbegynte søknader fra DigiSos hvis aktivert`() {
        val fakeUnleash = FakeUnleash().apply {
            enable(UnleashService.digisosPaabegynteToggleName)
        }
        val unleashService = UnleashService(fakeUnleash)

        val beskjedMerger = BeskjedMergerService(beskjedService, digiSosService, unleashService)

        val result = runBlocking {
            beskjedMerger.getActiveEvents(innloggetBruker)
        }

        coVerify(exactly = 1) { beskjedService.getActiveBeskjedEvents(any()) }
        coVerify(exactly = 1) { digiSosService.getPaabegynteActive(any()) }

        confirmVerified(beskjedService)
        confirmVerified(digiSosService)

        result.successFullSources().size shouldBe 2
        result.successFullSources() shouldBe listOf(KildeType.EVENTHANDLER, KildeType.DIGISOS)
    }

    @Test
    fun `Hent aktive fra alle kilder hvis aktivert`() {
        val fakeUnleash = FakeUnleash().apply {
            enable(UnleashService.digisosPaabegynteToggleName)
        }
        val unleashService = UnleashService(fakeUnleash)

        val beskjedMerger = BeskjedMergerService(beskjedService, digiSosService, unleashService)

        val result = runBlocking {
            beskjedMerger.getActiveEvents(innloggetBruker)
        }

        coVerify(exactly = 1) { beskjedService.getActiveBeskjedEvents(any()) }
        coVerify(exactly = 1) { digiSosService.getPaabegynteActive(any()) }

        confirmVerified(beskjedService)
        confirmVerified(digiSosService)

        result.successFullSources().size shouldBe 2
        result.successFullSources() shouldBe listOf(KildeType.EVENTHANDLER, KildeType.DIGISOS)
    }

    @Test
    fun `Hent inaktive alltid fra event-handler`() {
        val fakeUnleash = FakeUnleash()
        val unleashService = UnleashService(fakeUnleash)

        val beskjedMerger = BeskjedMergerService(beskjedService, digiSosService, unleashService)

        val result = runBlocking {
            beskjedMerger.getInactiveEvents(innloggetBruker)
        }

        coVerify(exactly = 1) { beskjedService.getInactiveBeskjedEvents(any()) }
        coVerify(exactly = 0) { digiSosService.getPaabegynteInactive(any()) }

        confirmVerified(beskjedService)
        confirmVerified(digiSosService)

        result.successFullSources().size shouldBe 1
        result.successFullSources() shouldBe listOf(KildeType.EVENTHANDLER)
    }

    @Test
    fun `Hent inaktive fra DigiSos hvis aktivert`() {
        val fakeUnleash = FakeUnleash().apply {
            enable(UnleashService.digisosPaabegynteToggleName)
        }
        val unleashService = UnleashService(fakeUnleash)

        val beskjedMerger = BeskjedMergerService(beskjedService, digiSosService, unleashService)

        val result = runBlocking {
            beskjedMerger.getInactiveEvents(innloggetBruker)
        }

        coVerify(exactly = 1) { beskjedService.getInactiveBeskjedEvents(any()) }
        coVerify(exactly = 1) { digiSosService.getPaabegynteInactive(any()) }

        confirmVerified(beskjedService)
        confirmVerified(digiSosService)

        result.successFullSources().size shouldBe 2
        result.successFullSources() shouldBe listOf(KildeType.EVENTHANDLER, KildeType.DIGISOS)
    }

    @Test
    fun `Hent inaktive fra alle kilder hvis aktivert`() {
        val fakeUnleash = FakeUnleash().apply {
            enable(UnleashService.digisosPaabegynteToggleName)
        }
        val unleashService = UnleashService(fakeUnleash)
        val beskjedMerger = BeskjedMergerService(beskjedService, digiSosService, unleashService)

        val result = runBlocking {
            beskjedMerger.getInactiveEvents(innloggetBruker)
        }

        coVerify(exactly = 1) { beskjedService.getInactiveBeskjedEvents(any()) }
        coVerify(exactly = 1) { digiSosService.getPaabegynteInactive(any()) }

        confirmVerified(beskjedService)
        confirmVerified(digiSosService)

        result.successFullSources().size shouldBe 2
        result.successFullSources() shouldBe listOf(KildeType.EVENTHANDLER, KildeType.DIGISOS)
    }
}
