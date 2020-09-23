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
    fun `Skal mottat en liste med VarselDTO i det aktive eventer hentes`() {
        val varsel1 = createUlestVarsel("1")
        val varsel2 = createUlestVarsel("2")
        coEvery { varselConsumer.getSisteVarsler(innloggetBruker) } returns listOf(varsel1, varsel2)
        runBlocking {
            val varselList = varselService.getActiveVarselEvents(innloggetBruker)
            varselList.size `should be equal to` 2
        }
    }

    @Test
    fun `Skal motta en liste med VarselDTO i det inaktive eventer hentes`() {
        val varsel1 = createLestVarsel("3")
        val varsel2 = createLestVarsel("4")
        coEvery { varselConsumer.getSisteVarsler(innloggetBruker) } returns listOf(varsel1, varsel2)
        runBlocking {
            val varselList = varselService.getInactiveVarselEvents(innloggetBruker)
            varselList.size `should be equal to` 2
        }
    }

    @Test
    fun `Skal kaste en exception hvis henting av aktive eventer feiler`() {
        coEvery { varselConsumer.getSisteVarsler(innloggetBruker) } throws Exception("error")
        invoking { runBlocking { varselService.getActiveVarselEvents(innloggetBruker) } } `should throw` ConsumeEventException::class
    }

    @Test
    fun `Skal kaste en exception hvis henting av inaktive eventer feiler`() {
        coEvery { varselConsumer.getSisteVarsler(innloggetBruker) } throws Exception("error")
        invoking { runBlocking { varselService.getInactiveVarselEvents(innloggetBruker) } } `should throw` ConsumeEventException::class
    }

}