package no.nav.personbruker.dittnav.api.informasjon

import no.nav.personbruker.dittnav.api.brukernotifikasjon.BrukernotifikasjonType
import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.`should be`
import org.junit.jupiter.api.Test

class InformasjonTransformerTest {

    @Test
    fun `should transform from Informasjon to Brukernotifikasjon`() {
        val informasjon1 = InformasjonObjectMother.createInformasjon("1", "1")
        val informasjon2 = InformasjonObjectMother.createInformasjon("2", "2")
        val brukernotifikasjonList = InformasjonTransformer.toBrukernotifikasjonList(listOf(informasjon1, informasjon2))
        val brukernotifikasjon = brukernotifikasjonList.get(0)

        brukernotifikasjon.produsent `should be equal to` informasjon1.produsent
        brukernotifikasjon.eventTidspunkt `should be` informasjon1.eventTidspunkt
        brukernotifikasjon.eventId `should be equal to` informasjon1.eventId
        brukernotifikasjon.tekst `should be equal to` informasjon1.tekst
        brukernotifikasjon.link `should be equal to` informasjon1.link
        brukernotifikasjon.sistOppdatert `should be` informasjon1.sistOppdatert
        brukernotifikasjon.type `should be` BrukernotifikasjonType.INFORMASJON
    }
}
