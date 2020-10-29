package no.nav.personbruker.dittnav.api.beskjed

import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import no.nav.personbruker.dittnav.api.common.InnloggetBrukerObjectMother
import no.nav.personbruker.dittnav.api.varsel.VarselService
import org.amshove.kluent.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class MergeBeskjedMedVarselServiceTest {

    private val beskjedService = mockk<BeskjedService>()
    private val varselService = mockk<VarselService>()

    private val innloggetBruker = InnloggetBrukerObjectMother.createInnloggetBruker()

    @BeforeEach
    fun init() {
        clearMocks(beskjedService, varselService)
    }

    @Test
    fun `Skal vise aktive beskjeder og varsler sammen`() {
        val expectedBeskjeder = BeskjedResultObjectMother.createNumberOfInactiveBeskjed(1)
        val expectedVarslerAsBeskjed = BeskjedResultObjectMother.createNumberOfInactiveBeskjed(2, "varsel")
        coEvery { beskjedService.getActiveBeskjedEvents(any()) } returns expectedBeskjeder
        coEvery { varselService.getActiveVarselEvents(any()) } returns expectedVarslerAsBeskjed

        val service = MergeBeskjedMedVarselService(beskjedService, varselService)

        val result = runBlocking {
            service.getActiveEvents(innloggetBruker)
        }

        result.shouldNotBeNull()
        result.results().size `should be` expectedBeskjeder.results().size + expectedVarslerAsBeskjed.results().size
        result.hasErrors().`should be false`()
        result.errors().`should be empty`()
    }

    @Test
    fun `Skal vise inaktive beskjeder og varsler sammen`() {
        val expectedBeskjeder = BeskjedResultObjectMother.createNumberOfInactiveBeskjed(2)
        val expectedVarslerAsBeskjed = BeskjedResultObjectMother.createNumberOfInactiveBeskjed(3, "varsel")
        coEvery { beskjedService.getActiveBeskjedEvents(any()) } returns expectedBeskjeder
        coEvery { varselService.getActiveVarselEvents(any()) } returns expectedVarslerAsBeskjed

        val service = MergeBeskjedMedVarselService(beskjedService, varselService)

        val result = runBlocking {
            service.getActiveEvents(innloggetBruker)
        }

        result.shouldNotBeNull()
        result.results().size `should be` expectedBeskjeder.results().size + expectedVarslerAsBeskjed.results().size
        result.hasErrors().`should be false`()
        result.errors().`should be empty`()
    }

    @Test
    fun `Skal returnere returnere svar og info om feil for aktive eventer, varselinnboks feiler`() {
        val expectedBeskjeder = BeskjedResultObjectMother.createNumberOfActiveBeskjed(2)
        coEvery { beskjedService.getActiveBeskjedEvents(any()) } returns expectedBeskjeder
        coEvery { varselService.getActiveVarselEvents(any()) } returns BeskjedResult(emptyList(), listOf(KildeType.VARSELINNBOKS))

        val service = MergeBeskjedMedVarselService(beskjedService, varselService)

        val beskjedResult = runBlocking {
            service.getActiveEvents(innloggetBruker)
        }

        beskjedResult.results().size `should be equal to` expectedBeskjeder.results().size
        beskjedResult.hasErrors().`should be true`()
        beskjedResult.errors() `should contain` KildeType.VARSELINNBOKS
    }

    @Test
    fun `Skal returnere returnere svar og info om feil for aktive eventer, event-handler feiler`() {
        val expectedBeskjeder = BeskjedResultObjectMother.createNumberOfActiveBeskjed(2)
        coEvery { beskjedService.getActiveBeskjedEvents(any()) } returns BeskjedResult(emptyList(), listOf(KildeType.EVENTHANDLER))
        coEvery { varselService.getActiveVarselEvents(any()) } returns expectedBeskjeder

        val service = MergeBeskjedMedVarselService(beskjedService, varselService)

        val beskjedResult = runBlocking {
            service.getActiveEvents(innloggetBruker)
        }

        beskjedResult.results().size `should be equal to` expectedBeskjeder.results().size
        beskjedResult.hasErrors().`should be true`()
        beskjedResult.errors() `should contain` KildeType.EVENTHANDLER
    }

    @Test
    fun `Skal returnere returnere svar og info om feil for inaktive eventer, varselinnboks feiler`() {
        val expectedBeskjeder = BeskjedResultObjectMother.createNumberOfActiveBeskjed(2)
        coEvery { beskjedService.getInactiveBeskjedEvents(any()) } returns expectedBeskjeder
        coEvery { varselService.getInactiveVarselEvents(any()) } returns BeskjedResult(emptyList(), listOf(KildeType.VARSELINNBOKS))

        val service = MergeBeskjedMedVarselService(beskjedService, varselService)

        val beskjedResult = runBlocking {
            service.getInactiveEvents(innloggetBruker)
        }

        beskjedResult.results().size `should be equal to` expectedBeskjeder.results().size
        beskjedResult.hasErrors().`should be true`()
        beskjedResult.errors() `should contain` KildeType.VARSELINNBOKS
    }

    @Test
    fun `Skal returnere returnere svar og info om feil for inaktive eventer, event-handler feiler`() {
        val expectedBeskjeder = BeskjedResultObjectMother.createNumberOfActiveBeskjed(2)
        coEvery { beskjedService.getInactiveBeskjedEvents(any()) } returns BeskjedResult(emptyList(), listOf(KildeType.EVENTHANDLER))
        coEvery { varselService.getInactiveVarselEvents(any()) } returns expectedBeskjeder

        val service = MergeBeskjedMedVarselService(beskjedService, varselService)

        val beskjedResult = runBlocking {
            service.getInactiveEvents(innloggetBruker)
        }

        beskjedResult.results().size `should be equal to` expectedBeskjeder.results().size
        beskjedResult.hasErrors().`should be true`()
        beskjedResult.errors() `should contain` KildeType.EVENTHANDLER
    }

    @Test
    fun `Skal stotte at begge kilder feiler for aktive eventer`() {
        coEvery { beskjedService.getActiveBeskjedEvents(any()) } returns BeskjedResult(emptyList(), listOf(KildeType.EVENTHANDLER))
        coEvery { varselService.getActiveVarselEvents(any()) } returns BeskjedResult(emptyList(), listOf(KildeType.VARSELINNBOKS))

        val service = MergeBeskjedMedVarselService(beskjedService, varselService)

        val beskjedResult = runBlocking {
            service.getActiveEvents(innloggetBruker)
        }

        beskjedResult.results().isEmpty().`should be true`()
        beskjedResult.hasErrors().`should be true`()
        beskjedResult.errors() `should contain` KildeType.EVENTHANDLER
        beskjedResult.errors() `should contain` KildeType.VARSELINNBOKS
    }

    @Test
    fun `Skal stotte at begge kilder feiler for inaktive eventer`() {
        coEvery { beskjedService.getInactiveBeskjedEvents(any()) } returns BeskjedResult(emptyList(), listOf(KildeType.EVENTHANDLER))
        coEvery { varselService.getInactiveVarselEvents(any()) } returns BeskjedResult(emptyList(), listOf(KildeType.VARSELINNBOKS))

        val service = MergeBeskjedMedVarselService(beskjedService, varselService)

        val beskjedResult = runBlocking {
            service.getInactiveEvents(innloggetBruker)
        }

        beskjedResult.results().isEmpty().`should be true`()
        beskjedResult.hasErrors().`should be true`()
        beskjedResult.errors() `should contain` KildeType.EVENTHANDLER
        beskjedResult.errors() `should contain` KildeType.VARSELINNBOKS
    }

}
