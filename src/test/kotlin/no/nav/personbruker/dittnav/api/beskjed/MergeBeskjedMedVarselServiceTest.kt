package no.nav.personbruker.dittnav.api.beskjed

import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import no.nav.personbruker.dittnav.common.test.`with message containing`
import no.nav.personbruker.dittnav.api.common.ConsumeEventException
import no.nav.personbruker.dittnav.api.common.InnloggetBrukerObjectMother
import no.nav.personbruker.dittnav.api.varsel.VarselService
import org.amshove.kluent.`should be`
import org.amshove.kluent.`should throw`
import org.amshove.kluent.invoking
import org.amshove.kluent.shouldNotBeNull
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
        val expectedBeskjeder = BeskjedDtoObjectMother.createNumberOfInactiveBeskjed(1)
        val expectedVarslerAsBeskjed = BeskjedDtoObjectMother.createNumberOfInactiveBeskjed(2, "varsel")
        coEvery { beskjedService.getActiveBeskjedEvents(any()) } returns expectedBeskjeder
        coEvery { varselService.getActiveVarselEvents(any()) } returns expectedVarslerAsBeskjed

        val service = MergeBeskjedMedVarselService(beskjedService, varselService)

        val alleAktive = runBlocking {
            service.getActiveEvents(innloggetBruker)
        }

        alleAktive.shouldNotBeNull()
        alleAktive.size `should be` expectedBeskjeder.size + expectedVarslerAsBeskjed.size
    }

    @Test
    fun `Skal vise inaktive beskjeder og varsler sammen`() {
        val expectedBeskjeder = BeskjedDtoObjectMother.createNumberOfInactiveBeskjed(2)
        val expectedVarslerAsBeskjed = BeskjedDtoObjectMother.createNumberOfInactiveBeskjed(3, "varsel")
        coEvery { beskjedService.getActiveBeskjedEvents(any()) } returns expectedBeskjeder
        coEvery { varselService.getActiveVarselEvents(any()) } returns expectedVarslerAsBeskjed

        val service = MergeBeskjedMedVarselService(beskjedService, varselService)

        val alleInaktive = runBlocking {
            service.getActiveEvents(innloggetBruker)
        }

        alleInaktive.shouldNotBeNull()
        alleInaktive.size `should be` expectedBeskjeder.size + expectedVarslerAsBeskjed.size
    }

    @Test
    fun `Skal kaste intern exception hvis minst en av kildene for aktive eventer feiler`() {
        coEvery { beskjedService.getActiveBeskjedEvents(any()) } throws Exception("Simulert feil")
        coEvery { varselService.getActiveVarselEvents(any()) } returns emptyList()

        val service = MergeBeskjedMedVarselService(beskjedService, varselService)

        invoking {
            runBlocking {
                service.getActiveEvents(innloggetBruker)
            }
        } `should throw` ConsumeEventException::class `with message containing` "Uventet feil ved sammenslåing av aktive"
    }

    @Test
    fun `Skal kaste intern exception hvis minst en av kildene for inaktive eventer feiler`() {
        coEvery { beskjedService.getActiveBeskjedEvents(any()) } returns emptyList()
        coEvery { varselService.getActiveVarselEvents(any()) } throws Exception("Simulert feil")

        val service = MergeBeskjedMedVarselService(beskjedService, varselService)

        invoking {
            runBlocking {
                service.getInactiveEvents(innloggetBruker)
            }
        } `should throw` ConsumeEventException::class `with message containing` "Uventet feil ved sammenslåing av inaktive"
    }

}
