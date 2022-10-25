package no.nav.personbruker.dittnav.api.beskjed

import io.kotest.matchers.shouldBe
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import no.nav.personbruker.dittnav.api.TestUser
import no.nav.personbruker.dittnav.api.digisos.DigiSosService
import no.nav.personbruker.dittnav.api.getNumberOfSuccessfulBeskjedEventsForSource
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class BeskjedMergerServiceTest {

    private val beskjedService = mockk<BeskjedConsumer>(relaxed = true)
    private val digiSosService = mockk<DigiSosService>(relaxed = true)

    private val innloggetBruker = TestUser.createAuthenticatedUser()

    private val eventHandlerDefaultResult =
        getNumberOfSuccessfulBeskjedEventsForSource(
            1,
            KildeType.EVENTHANDLER,
            "handler"
        )

    private val digiSosDefaultResult = getNumberOfSuccessfulBeskjedEventsForSource(
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
    fun `Henter aktiver beskjeder fra eventhandler og digisos`() {

        val beskjedMerger = BeskjedMergerService(beskjedService, digiSosService)
        val result = runBlocking {
            beskjedMerger.getActiveEvents(innloggetBruker)
        }

        result.successFullSources().size shouldBe 2
        result.successFullSources() shouldBe listOf(KildeType.EVENTHANDLER, KildeType.DIGISOS)
    }

    @Test
    fun `Henter inaktive beskjeder fra eventhandler og digisos`() {

        val beskjedMerger = BeskjedMergerService(beskjedService, digiSosService)

        val result = runBlocking {
            beskjedMerger.getInactiveEvents(innloggetBruker)
        }

        result.successFullSources().size shouldBe 2
        result.successFullSources() shouldBe listOf(KildeType.EVENTHANDLER, KildeType.DIGISOS)
    }
}
