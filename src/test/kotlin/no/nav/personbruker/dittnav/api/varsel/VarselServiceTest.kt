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
        val varsel1 = createActiveVarsel("1")
        val varsel2 = createActiveVarsel("2")
        coEvery { varselConsumer.getSisteVarsler(innloggetBruker) } returns listOf(varsel1, varsel2)
        runBlocking {
            val varselList = varselService.getActiveVarselEvents(innloggetBruker)
            varselList.size `should be equal to` 2
        }
    }

    @Test
    fun `should return list of VarselDTO when inactive Events are received`() {
        val varsel1 = createInactiveVarsel("3")
        val varsel2 = createInactiveVarsel("4")
        coEvery { varselConsumer.getSisteVarsler(innloggetBruker) } returns listOf(varsel1, varsel2)
        runBlocking {
            val varselList = varselService.getInactiveVarselEvents(innloggetBruker)
            varselList.size `should be equal to` 2
        }
    }

    @Test
    fun `should mask events with security level higher than current user`() {
        val ident = "1"
        val varsel = createActiveVarsel("5")
        innloggetBruker = InnloggetBrukerObjectMother.createInnloggetBruker(ident, 3)
        coEvery { varselConsumer.getSisteVarsler(innloggetBruker) } returns listOf(varsel)
        runBlocking {
            val varselList = varselService.getActiveVarselEvents(innloggetBruker)
            val varselDTO = varselList.first()
            varselDTO.tekst `should be equal to` "***"
            varselDTO.link `should be equal to` "***"
            varselDTO.sikkerhetsnivaa `should be equal to` 4
        }
    }

    @Test
    fun `should not mask events with security level lower than current user`() {
        val varsel = createActiveVarsel("6")
        coEvery { varselConsumer.getSisteVarsler(innloggetBruker) } returns listOf(varsel)
        runBlocking {
            val varselList = varselService.getActiveVarselEvents(innloggetBruker)
            val varselDTO = varselList.first()
            varselDTO.tekst `should be equal to` varsel.varseltekst
            varselDTO.link `should be equal to` varsel.url
        }
    }

    @Test
    fun `should not mask events with security level equal than current user`() {
        val varsel = createActiveVarsel("7")
        coEvery { varselConsumer.getSisteVarsler(innloggetBruker) } returns listOf(varsel)
        runBlocking {
            val varselList = varselService.getActiveVarselEvents(innloggetBruker)
            val beskjedDTO = varselList.first()
            beskjedDTO.tekst `should be equal to` varsel.varseltekst
            beskjedDTO.link `should be equal to` varsel.url
            beskjedDTO.sikkerhetsnivaa `should be equal to` 4
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
