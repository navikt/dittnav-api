package no.nav.personbruker.dittnav.api.innboks

import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.`should be`
import org.junit.jupiter.api.Test

class InnboksTransformerTest {

    @Test
    fun `should transform from Innboks to Brukernotifikasjon`() {
        val innboks1 = createInnboks("1", "1")
        val innboks2 = createInnboks("2", "2")
        val innboksDTOList = listOf(innboks1, innboks2).map { toInnboksDTO(it) }
        val innboksDTO = innboksDTOList.first()

        innboksDTO.produsent `should be equal to` innboks1.produsent
        innboksDTO.eventTidspunkt `should be` innboks1.eventTidspunkt
        innboksDTO.eventId `should be equal to` innboks1.eventId
        innboksDTO.tekst `should be equal to` innboks1.tekst
        innboksDTO.link `should be equal to` innboks1.link
        innboksDTO.sistOppdatert `should be` innboks1.sistOppdatert
    }
}
