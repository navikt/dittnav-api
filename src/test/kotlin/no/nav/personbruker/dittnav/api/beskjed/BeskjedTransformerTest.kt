package no.nav.personbruker.dittnav.api.beskjed

import no.nav.personbruker.dittnav.api.brukernotifikasjon.BrukernotifikasjonType
import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.`should be`
import org.junit.jupiter.api.Test

class BeskjedTransformerTest {

    @Test
    fun `should transform from Beskjed to Brukernotifikasjon`() {
        val beskjed1 = BeskjedObjectMother.createBeskjed("1", "1", "1")
        val beskjed2 = BeskjedObjectMother.createBeskjed("2", "2","2")
        val brukernotifikasjonList = listOf(beskjed1, beskjed2).map { toBrukernotifikasjon(it) }
        val brukernotifikasjon = brukernotifikasjonList.first()

        brukernotifikasjon.eventTidspunkt `should be` beskjed1.eventTidspunkt
        brukernotifikasjon.eventId `should be equal to` beskjed1.eventId
        brukernotifikasjon.tekst `should be equal to` beskjed1.tekst
        brukernotifikasjon.link `should be equal to` beskjed1.link
        brukernotifikasjon.sistOppdatert `should be` beskjed1.sistOppdatert
        brukernotifikasjon.type `should be` BrukernotifikasjonType.BESKJED
    }
}
