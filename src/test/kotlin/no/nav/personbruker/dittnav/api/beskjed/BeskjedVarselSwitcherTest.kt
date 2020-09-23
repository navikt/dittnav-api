package no.nav.personbruker.dittnav.api.beskjed

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import no.nav.personbruker.dittnav.api.common.InnloggetBrukerObjectMother
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class BeskjedVarselSwitcherTest {

    private val beskjedService = mockk<BeskjedService>(relaxed = true)
    private val beskjedMedVarselService = mockk<MergeBeskjedMedVarselService>(relaxed = true)

    private val dummyUser = InnloggetBrukerObjectMother.createInnloggetBruker()

    @BeforeEach
    fun setup() {
        coEvery { beskjedService.getActiveBeskjedEvents(any()) } returns emptyList()
        coEvery { beskjedService.getInactiveBeskjedEvents(any()) } returns emptyList()
        coEvery { beskjedMedVarselService.getActiveEvents(any()) } returns emptyList()
        coEvery { beskjedMedVarselService.getInactiveEvents(any()) } returns emptyList()
    }

    @Test
    fun `Skal kun bruke beskjedMedVarselService hvis varsler er aktivert`() {
        val serviceMedVarsel = BeskjedVarselSwitcher(beskjedService, beskjedMedVarselService, true)

        runBlocking {
            serviceMedVarsel.getActiveEvents(dummyUser)
            serviceMedVarsel.getInactiveEvents(dummyUser)
        }
        coVerify(exactly = 1) { beskjedMedVarselService.getActiveEvents(any()) }
        coVerify(exactly = 1) { beskjedMedVarselService.getInactiveEvents(any()) }

        coVerify(exactly = 0) { beskjedService.getActiveBeskjedEvents(any()) }
        coVerify(exactly = 0) { beskjedService.getInactiveBeskjedEvents(any()) }
    }

    @Test
    fun `Skal kun bruke beskjedService hvis varsler er deaktivert`() {
        val serviceUtenVarsel = BeskjedVarselSwitcher(beskjedService, beskjedMedVarselService, false)

        runBlocking {
            serviceUtenVarsel.getActiveEvents(dummyUser)
            serviceUtenVarsel.getInactiveEvents(dummyUser)
        }
        coVerify(exactly = 1) { beskjedService.getActiveBeskjedEvents(any()) }
        coVerify(exactly = 1) { beskjedService.getInactiveBeskjedEvents(any()) }

        coVerify(exactly = 0) { beskjedMedVarselService.getActiveEvents(any()) }
        coVerify(exactly = 0) { beskjedMedVarselService.getInactiveEvents(any()) }
    }

}
