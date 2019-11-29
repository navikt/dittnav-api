package no.nav.personbruker.dittnav.api.beskjed

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.amshove.kluent.`should be equal to`
import org.junit.jupiter.api.Test


class BeskjedServiceTest {

    val beskjedConsumer = mockk<BeskjedConsumer>()
    val beskjedService = BeskjedService(beskjedConsumer)
    val beskjed1 = BeskjedObjectMother.createBeskjed("1", "1")
    val beskjed2 = BeskjedObjectMother.createBeskjed("2", "2")

    @Test
    fun `should return list of Brukernotifikasjoner when Events are received`() {
        coEvery { beskjedConsumer.getExternalEvents("1234") } returns listOf(beskjed1, beskjed2)

        runBlocking {
            val brukernotifikasjonListe = beskjedService.getBeskjedEventsAsBrukernotifikasjoner("1234")
            brukernotifikasjonListe.size `should be equal to` 2
        }

    }

    @Test
    fun `should return empty list when Exception is thrown`() {
        coEvery { beskjedConsumer.getExternalEvents("1234") } throws Exception("error")

        runBlocking {
            val brukernotifikasjonListe = beskjedService.getBeskjedEventsAsBrukernotifikasjoner("1234")
            brukernotifikasjonListe.size `should be equal to` 0
        }

    }

}