package no.nav.personbruker.dittnav.api.informasjon

import no.nav.personbruker.dittnav.api.config.EventType
import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.`should be`
import org.junit.jupiter.api.Test

class InformasjonTransformerTest {

    @Test
    fun `should transform from Informasjon to Brukernotifikasjon`() {
        val informasjon1 = InformasjonObjectMother.createInformasjon("1", "1")
        val informasjon2 = InformasjonObjectMother.createInformasjon("2", "2")
        val brukernotifikasjonList = InformasjonTransformer.toBrukernotifikasjonList(listOf(informasjon1, informasjon2))
        val brukernotifikasjon1 = brukernotifikasjonList.get(0)

        brukernotifikasjon1.produsent `should be equal to` informasjon1.produsent
        brukernotifikasjon1.eventTidspunkt `should be` informasjon1.eventTidspunkt
        brukernotifikasjon1.eventId `should be equal to` informasjon1.eventId
        brukernotifikasjon1.tekst `should be equal to` informasjon1.tekst
        brukernotifikasjon1.link `should be equal to` informasjon1.link
        brukernotifikasjon1.sistOppdatert `should be` informasjon1.sistOppdatert
        brukernotifikasjon1.type `should be` EventType.INFORMASJON
    }
}
