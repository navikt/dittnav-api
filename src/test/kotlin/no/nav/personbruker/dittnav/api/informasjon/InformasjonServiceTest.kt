package no.nav.personbruker.dittnav.api.informasjon

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.amshove.kluent.`should be equal to`
import org.junit.jupiter.api.Test


class InformasjonServiceTest {

    val informasjonConsumer = mockk<InformasjonConsumer>()
    val informasjonService = InformasjonService(informasjonConsumer)
    val informasjon1 = InformasjonObjectMother.createInformasjon("1", "1")
    val informasjon2 = InformasjonObjectMother.createInformasjon("2", "2")

    @Test
    fun `should return list of Brukernotifikasjoner when Events are received`() {
        coEvery { informasjonConsumer.getExternalEvents("1234") } returns listOf(informasjon1, informasjon2)

        runBlocking {
            val brukernotifikasjonListe = informasjonService.getInformasjonEventsAsBrukernotifikasjoner("1234")
            brukernotifikasjonListe.size `should be equal to` 2
        }

    }

    @Test
    fun `should return empty list when Exception is thrown`() {
        coEvery { informasjonConsumer.getExternalEvents("1234") } throws Exception("error")

        runBlocking {
            val brukernotifikasjonListe = informasjonService.getInformasjonEventsAsBrukernotifikasjoner("1234")
            brukernotifikasjonListe.size `should be equal to` 0
        }

    }

}
