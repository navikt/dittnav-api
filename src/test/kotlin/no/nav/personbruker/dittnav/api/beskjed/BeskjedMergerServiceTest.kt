package no.nav.personbruker.dittnav.api.beskjed

import io.mockk.*
import kotlinx.coroutines.runBlocking
import no.nav.personbruker.dittnav.api.common.AuthenticatedUserObjectMother
import no.nav.personbruker.dittnav.api.common.MultiSourceResultObjectMother
import no.nav.personbruker.dittnav.api.digisos.DigiSosService
import no.nav.personbruker.dittnav.api.unleash.UnleashService
import no.nav.personbruker.dittnav.api.varsel.VarselService
import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.`should contain all`
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class BeskjedMergerServiceTest {

    private val beskjedService = mockk<BeskjedService>(relaxed = true)
    private val varselService = mockk<VarselService>(relaxed = true)
    private val digiSosService = mockk<DigiSosService>(relaxed = true)
    private val unleashService = mockk<UnleashService>(relaxed = true)

    private val innloggetBruker = AuthenticatedUserObjectMother.createAuthenticatedUser()

    private val eventHandlerDefaultResult = MultiSourceResultObjectMother.giveMeNumberOfSuccessEventsForSource(
        1,
        KildeType.EVENTHANDLER,
        "handler"
    )

    private val varslerDefaultResult = MultiSourceResultObjectMother.giveMeNumberOfSuccessEventsForSource(
        2,
        KildeType.VARSELINNBOKS,
        "varsel"
    )

    private val digiSosDefaultResult = MultiSourceResultObjectMother.giveMeNumberOfSuccessEventsForSource(
        3,
        KildeType.DIGISOS,
        "digiSos"
    )

    @BeforeEach
    fun init() {
        clearMocks(beskjedService, varselService, digiSosService, unleashService)

        coEvery { beskjedService.getActiveBeskjedEvents(any()) } returns eventHandlerDefaultResult
        coEvery { beskjedService.getInactiveBeskjedEvents(any()) } returns eventHandlerDefaultResult

        coEvery { varselService.getActiveVarselEvents(any()) } returns varslerDefaultResult
        coEvery { varselService.getInactiveVarselEvents(any()) } returns varslerDefaultResult

        coEvery { digiSosService.getActiveEvents(any()) } returns digiSosDefaultResult
        coEvery { digiSosService.getInactiveEvents(any()) } returns digiSosDefaultResult
    }

    @Test
    fun `Hent aktive alltid fra event-handler`() {
        coEvery { unleashService.mergeBeskjedVarselEnabled(any()) } returns false
        coEvery { unleashService.digiSosEnabled(any()) } returns false

        val beskjedMerger = BeskjedMergerService(beskjedService, varselService, digiSosService, unleashService)

        val result = runBlocking {
            beskjedMerger.getActiveEvents(innloggetBruker)
        }

        coVerify(exactly = 1) { beskjedService.getActiveBeskjedEvents(any()) }
        coVerify(exactly = 0) { varselService.getActiveVarselEvents(any()) }
        coVerify(exactly = 0) { digiSosService.getActiveEvents(any()) }

        coVerify(exactly = 1) { unleashService.mergeBeskjedVarselEnabled(any()) }
        coVerify(exactly = 1) { unleashService.digiSosEnabled(any()) }

        confirmVerified(beskjedService)
        confirmVerified(varselService)
        confirmVerified(digiSosService)
        confirmVerified(unleashService)

        result.successFullSources().size `should be equal to` 1
        result.successFullSources() `should contain all` listOf(KildeType.EVENTHANDLER)
    }

    @Test
    fun `Hent aktive fra Varselinnboks hvis aktivert`() {
        coEvery { unleashService.mergeBeskjedVarselEnabled(any()) } returns true
        coEvery { unleashService.digiSosEnabled(any()) } returns false

        val beskjedMerger = BeskjedMergerService(beskjedService, varselService, digiSosService, unleashService)

        val result = runBlocking {
            beskjedMerger.getActiveEvents(innloggetBruker)
        }

        coVerify(exactly = 1) { beskjedService.getActiveBeskjedEvents(any()) }
        coVerify(exactly = 1) { varselService.getActiveVarselEvents(any()) }
        coVerify(exactly = 0) { digiSosService.getActiveEvents(any()) }

        coVerify(exactly = 1) { unleashService.mergeBeskjedVarselEnabled(any()) }
        coVerify(exactly = 1) { unleashService.digiSosEnabled(any()) }

        confirmVerified(beskjedService)
        confirmVerified(varselService)
        confirmVerified(digiSosService)
        confirmVerified(unleashService)

        result.successFullSources().size `should be equal to` 2
        result.successFullSources() `should contain all` listOf(KildeType.EVENTHANDLER, KildeType.VARSELINNBOKS)
    }

    @Test
    fun `Hent aktive fra DigiSos hvis aktivert`() {
        coEvery { unleashService.mergeBeskjedVarselEnabled(any()) } returns false
        coEvery { unleashService.digiSosEnabled(any()) } returns true

        val beskjedMerger = BeskjedMergerService(beskjedService, varselService, digiSosService, unleashService)

        val result = runBlocking {
            beskjedMerger.getActiveEvents(innloggetBruker)
        }

        coVerify(exactly = 1) { beskjedService.getActiveBeskjedEvents(any()) }
        coVerify(exactly = 0) { varselService.getActiveVarselEvents(any()) }
        coVerify(exactly = 1) { digiSosService.getActiveEvents(any()) }

        coVerify(exactly = 1) { unleashService.mergeBeskjedVarselEnabled(any()) }
        coVerify(exactly = 1) { unleashService.digiSosEnabled(any()) }

        confirmVerified(beskjedService)
        confirmVerified(varselService)
        confirmVerified(digiSosService)
        confirmVerified(unleashService)

        result.successFullSources().size `should be equal to` 2
        result.successFullSources() `should contain all` listOf(KildeType.EVENTHANDLER, KildeType.DIGISOS)
    }

    @Test
    fun `Hent aktive fra alle kilder hvis aktivert`() {
        coEvery { unleashService.mergeBeskjedVarselEnabled(any()) } returns true
        coEvery { unleashService.digiSosEnabled(any()) } returns true

        val beskjedMerger = BeskjedMergerService(beskjedService, varselService, digiSosService, unleashService)

        val result = runBlocking {
            beskjedMerger.getActiveEvents(innloggetBruker)
        }

        coVerify(exactly = 1) { beskjedService.getActiveBeskjedEvents(any()) }
        coVerify(exactly = 1) { varselService.getActiveVarselEvents(any()) }
        coVerify(exactly = 1) { digiSosService.getActiveEvents(any()) }

        coVerify(exactly = 1) { unleashService.mergeBeskjedVarselEnabled(any()) }
        coVerify(exactly = 1) { unleashService.digiSosEnabled(any()) }

        confirmVerified(beskjedService)
        confirmVerified(varselService)
        confirmVerified(digiSosService)
        confirmVerified(unleashService)

        result.successFullSources().size `should be equal to` 3
        result.successFullSources() `should contain all` listOf(KildeType.EVENTHANDLER, KildeType.VARSELINNBOKS, KildeType.DIGISOS)
    }

    @Test
    fun `Hent inaktive alltid fra event-handler`() {
        coEvery { unleashService.mergeBeskjedVarselEnabled(any()) } returns false
        coEvery { unleashService.digiSosEnabled(any()) } returns false

        val beskjedMerger = BeskjedMergerService(beskjedService, varselService, digiSosService, unleashService)

        val result = runBlocking {
            beskjedMerger.getInactiveEvents(innloggetBruker)
        }

        coVerify(exactly = 1) { beskjedService.getInactiveBeskjedEvents(any()) }
        coVerify(exactly = 0) { varselService.getInactiveVarselEvents(any()) }
        coVerify(exactly = 0) { digiSosService.getInactiveEvents(any()) }

        coVerify(exactly = 1) { unleashService.mergeBeskjedVarselEnabled(any()) }
        coVerify(exactly = 1) { unleashService.digiSosEnabled(any()) }

        confirmVerified(beskjedService)
        confirmVerified(varselService)
        confirmVerified(digiSosService)
        confirmVerified(unleashService)

        result.successFullSources().size `should be equal to` 1
        result.successFullSources() `should contain all` listOf(KildeType.EVENTHANDLER)
    }

    @Test
    fun `Hent inaktive fra Varselinnboks hvis aktivert`() {
        coEvery { unleashService.mergeBeskjedVarselEnabled(any()) } returns true
        coEvery { unleashService.digiSosEnabled(any()) } returns false

        val beskjedMerger = BeskjedMergerService(beskjedService, varselService, digiSosService, unleashService)

        val result = runBlocking {
            beskjedMerger.getInactiveEvents(innloggetBruker)
        }

        coVerify(exactly = 1) { beskjedService.getInactiveBeskjedEvents(any()) }
        coVerify(exactly = 1) { varselService.getInactiveVarselEvents(any()) }
        coVerify(exactly = 0) { digiSosService.getInactiveEvents(any()) }

        coVerify(exactly = 1) { unleashService.mergeBeskjedVarselEnabled(any()) }
        coVerify(exactly = 1) { unleashService.digiSosEnabled(any()) }

        confirmVerified(beskjedService)
        confirmVerified(varselService)
        confirmVerified(digiSosService)
        confirmVerified(unleashService)

        result.successFullSources().size `should be equal to` 2
        result.successFullSources() `should contain all` listOf(KildeType.EVENTHANDLER, KildeType.VARSELINNBOKS)
    }

    @Test
    fun `Hent inaktive fra DigiSos hvis aktivert`() {
        coEvery { unleashService.mergeBeskjedVarselEnabled(any()) } returns false
        coEvery { unleashService.digiSosEnabled(any()) } returns true

        val beskjedMerger = BeskjedMergerService(beskjedService, varselService, digiSosService, unleashService)

        val result = runBlocking {
            beskjedMerger.getInactiveEvents(innloggetBruker)
        }

        coVerify(exactly = 1) { beskjedService.getInactiveBeskjedEvents(any()) }
        coVerify(exactly = 0) { varselService.getInactiveVarselEvents(any()) }
        coVerify(exactly = 1) { digiSosService.getInactiveEvents(any()) }

        coVerify(exactly = 1) { unleashService.mergeBeskjedVarselEnabled(any()) }
        coVerify(exactly = 1) { unleashService.digiSosEnabled(any()) }

        confirmVerified(beskjedService)
        confirmVerified(varselService)
        confirmVerified(digiSosService)
        confirmVerified(unleashService)

        result.successFullSources().size `should be equal to` 2
        result.successFullSources() `should contain all` listOf(KildeType.EVENTHANDLER, KildeType.DIGISOS)
    }

    @Test
    fun `Hent inaktive fra alle kilder hvis aktivert`() {
        coEvery { unleashService.mergeBeskjedVarselEnabled(any()) } returns true
        coEvery { unleashService.digiSosEnabled(any()) } returns true

        val beskjedMerger = BeskjedMergerService(beskjedService, varselService, digiSosService, unleashService)

        val result = runBlocking {
            beskjedMerger.getInactiveEvents(innloggetBruker)
        }

        coVerify(exactly = 1) { beskjedService.getInactiveBeskjedEvents(any()) }
        coVerify(exactly = 1) { varselService.getInactiveVarselEvents(any()) }
        coVerify(exactly = 1) { digiSosService.getInactiveEvents(any()) }

        coVerify(exactly = 1) { unleashService.mergeBeskjedVarselEnabled(any()) }
        coVerify(exactly = 1) { unleashService.digiSosEnabled(any()) }

        confirmVerified(beskjedService)
        confirmVerified(varselService)
        confirmVerified(digiSosService)
        confirmVerified(unleashService)

        result.successFullSources().size `should be equal to` 3
        result.successFullSources() `should contain all` listOf(KildeType.EVENTHANDLER, KildeType.VARSELINNBOKS, KildeType.DIGISOS)
    }

}
