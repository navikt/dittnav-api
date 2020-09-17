package no.nav.personbruker.dittnav.api.varsel

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import no.nav.personbruker.dittnav.api.common.ConsumeEventException
import no.nav.personbruker.dittnav.api.common.InnloggetBrukerObjectMother
import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.`should throw`
import org.amshove.kluent.invoking
import org.junit.jupiter.api.Test

class VarselServiceTest {

    var innloggetBruker = InnloggetBrukerObjectMother.createInnloggetBruker()

    val varselConsumer = mockk<VarselConsumer>()
    val varselService = VarselService(varselConsumer)

    @Test
    fun `should return list of VarselDTO when active Events are received`() {
        val varsel1 = createUlestVarsel("1")
        val varsel2 = createUlestVarsel("2")
        coEvery { varselConsumer.getSisteVarsler(innloggetBruker) } returns listOf(varsel1, varsel2)
        runBlocking {
            val varselList = varselService.getActiveVarselEvents(innloggetBruker)
            varselList.size `should be equal to` 2
        }
    }

    @Test
    fun `should return list of VarselDTO when inactive Events are received`() {
        val varsel1 = createLestVarsel("3")
        val varsel2 = createLestVarsel("4")
        coEvery { varselConsumer.getSisteVarsler(innloggetBruker) } returns listOf(varsel1, varsel2)
        runBlocking {
            val varselList = varselService.getInactiveVarselEvents(innloggetBruker)
            varselList.size `should be equal to` 2
        }
    }

    @Test
    fun `should throw exception if fetching active events fails`() {
        coEvery { varselConsumer.getSisteVarsler(innloggetBruker) } throws Exception("error")
        invoking { runBlocking { varselService.getActiveVarselEvents(innloggetBruker) } } `should throw` ConsumeEventException::class
    }

    @Test
    fun `should throw exception if fetching inactive events fails`() {
        coEvery { varselConsumer.getSisteVarsler(innloggetBruker) } throws Exception("error")
        invoking { runBlocking { varselService.getInactiveVarselEvents(innloggetBruker) } } `should throw` ConsumeEventException::class
    }

}
