package no.nav.personbruker.dittnav.api.varsel

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import no.nav.personbruker.dittnav.api.beskjed.KildeType
import no.nav.personbruker.dittnav.api.common.AuthenticatedUserObjectMother
import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.`should contain`
import org.junit.jupiter.api.Test

class VarselServiceTest {

    var user = AuthenticatedUserObjectMother.createAuthenticatedUser()

    val varselConsumer = mockk<VarselConsumer>()
    val varselService = VarselService(varselConsumer)

    @Test
    fun `Skal mottat en liste med VarselDTO i det aktive eventer hentes`() {
        val varsel1 = createUlestVarsel("1")
        val varsel2 = createUlestVarsel("2")
        coEvery { varselConsumer.getSisteVarsler(user) } returns listOf(varsel1, varsel2)
        runBlocking {
            val varselList = varselService.getActiveVarselEvents(user)
            varselList.results().size `should be equal to` 2
        }
    }

    @Test
    fun `Skal motta en liste med VarselDTO i det inaktive eventer hentes`() {
        val varsel1 = createLestVarsel("3")
        val varsel2 = createLestVarsel("4")
        coEvery { varselConsumer.getSisteVarsler(user) } returns listOf(varsel1, varsel2)
        runBlocking {
            val varselList = varselService.getInactiveVarselEvents(user)
            varselList.results().size `should be equal to` 2
        }
    }

    @Test
    fun `Skal kaste en exception hvis henting av aktive eventer feiler`() {
        coEvery { varselConsumer.getSisteVarsler(user) } throws Exception("error")
        runBlocking {
            val beskjedResult = varselService.getActiveVarselEvents(user)
            beskjedResult.hasErrors() `should be equal to` true
            beskjedResult.errors() `should contain` KildeType.VARSELINNBOKS
        }
    }

    @Test
    fun `Skal kaste en exception hvis henting av inaktive eventer feiler`() {
        coEvery { varselConsumer.getSisteVarsler(user) } throws Exception("error")
        runBlocking {
            val beskjedResult = varselService.getInactiveVarselEvents(user)
            beskjedResult.hasErrors() `should be equal to` true
            beskjedResult.errors() `should contain` KildeType.VARSELINNBOKS
        }
    }

}
