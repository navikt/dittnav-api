package no.nav.personbruker.dittnav.api.innboks

import no.nav.personbruker.dittnav.api.brukernotifikasjon.BrukernotifikasjonType
import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.`should be`
import org.junit.jupiter.api.Test

class InnboksTransformerTest {
    @Test
    fun `should transform from Innboks to Brukernotifikasjon`() {
        val innboks1 = createInnboks("1", "1")
        val innboks2 = createInnboks("2", "2")
        val brukernotifikasjonList = listOf(innboks1, innboks2).map { toBrukernotifikasjon(it) }
        val brukernotifikasjon = brukernotifikasjonList.first()

        brukernotifikasjon.eventTidspunkt `should be` innboks1.eventTidspunkt
        brukernotifikasjon.eventId `should be equal to` innboks1.eventId
        brukernotifikasjon.tekst `should be equal to` innboks1.tekst
        brukernotifikasjon.link `should be equal to` innboks1.link
        brukernotifikasjon.sistOppdatert `should be` innboks1.sistOppdatert
        brukernotifikasjon.type `should be` BrukernotifikasjonType.INNBOKS
    }
}