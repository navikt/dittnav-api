package no.nav.personbruker.dittnav.api.beskjed

import io.kotest.matchers.shouldBe
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import no.nav.personbruker.dittnav.api.TestUser
import no.nav.personbruker.dittnav.api.digisos.DigiSosConsumer
import no.nav.personbruker.dittnav.api.getNumberOfSuccessfulBeskjedEventsForSource
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class BeskjedMergerServiceTest {

    private val beskjedService = mockk<BeskjedConsumer>(relaxed = true)
    private val digiSosConsumer = mockk<DigiSosConsumer>(relaxed = true)

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
        clearMocks(beskjedService, digiSosConsumer)

        coEvery { beskjedService.getActiveBeskjedEvents(any()) } returns eventHandlerDefaultResult
        coEvery { beskjedService.getInactiveBeskjedEvents(any()) } returns eventHandlerDefaultResult

        coEvery { digiSosConsumer.getPaabegynteActive(any()) } returns digiSosDefaultResult
        coEvery { digiSosConsumer.getPaabegynteInactive(any()) } returns digiSosDefaultResult
    }

    @Test
    fun `Henter aktiver beskjeder fra eventhandler og digisos`() {

        val beskjedMerger = BeskjedMergerService(beskjedService, digiSosConsumer)
        val result = runBlocking {
            beskjedMerger.getActiveEvents(innloggetBruker)
        }

        result.successFullSources().size shouldBe 2
        result.successFullSources() shouldBe listOf(KildeType.EVENTHANDLER, KildeType.DIGISOS)
    }

    @Test
    fun `Henter inaktive beskjeder fra eventhandler og digisos`() {

        val beskjedMerger = BeskjedMergerService(beskjedService, digiSosConsumer)

        val result = runBlocking {
            beskjedMerger.getInactiveEvents(innloggetBruker)
        }

        result.successFullSources().size shouldBe 2
        result.successFullSources() shouldBe listOf(KildeType.EVENTHANDLER, KildeType.DIGISOS)
    }
}
