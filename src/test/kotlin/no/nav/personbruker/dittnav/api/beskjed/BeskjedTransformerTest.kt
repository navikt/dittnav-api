package no.nav.personbruker.dittnav.api.beskjed

import no.nav.personbruker.dittnav.api.brukernotifikasjon.BrukernotifikasjonType
import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.`should be`
import org.junit.jupiter.api.Test

class BeskjedTransformerTest {

    @Test
    fun `should transform from Beskjed to BeskjedDTO`() {
        val beskjed1 = createBeskjed("1", "1", "uid1", true)
        val beskjed2 = createBeskjed("2", "2", "uid2", true)
        val beskjedDTOList = listOf(beskjed1, beskjed2).map { toBeskjedDTO(it) }
        val beskjedDTO = beskjedDTOList.first()

        beskjedDTO.uid `should be` beskjed1.uid
        beskjedDTO.eventTidspunkt `should be` beskjed1.eventTidspunkt
        beskjedDTO.eventId `should be equal to` beskjed1.eventId
        beskjedDTO.tekst `should be equal to` beskjed1.tekst
        beskjedDTO.link `should be equal to` beskjed1.link
        beskjedDTO.produsent `should be equal to` beskjed1.produsent
        beskjedDTO.sistOppdatert `should be` beskjed1.sistOppdatert
        beskjedDTO.sikkerhetsnivaa `should be` beskjed1.sikkerhetsnivaa
    }

    @Test
    fun `should transform from Beskjed to Brukernotifikasjon`() {
        val beskjed1 = createBeskjed("1", "1", "1", true)
        val beskjed2 = createBeskjed("2", "2","2", true)
        val brukernotifikasjonList = listOf(beskjed1, beskjed2).map { toBrukernotifikasjon(it) }
        val brukernotifikasjon = brukernotifikasjonList.first()

        brukernotifikasjon.eventTidspunkt `should be` beskjed1.eventTidspunkt
        brukernotifikasjon.eventId `should be equal to` beskjed1.eventId
        brukernotifikasjon.tekst `should be equal to` beskjed1.tekst
        brukernotifikasjon.link `should be equal to` beskjed1.link
        brukernotifikasjon.sistOppdatert `should be` beskjed1.sistOppdatert
        brukernotifikasjon.sikkerhetsnivaa `should be` beskjed1.sikkerhetsnivaa
        brukernotifikasjon.type `should be` BrukernotifikasjonType.BESKJED
    }

    @Test
    fun `should mask tekst and link`() {
        val beskjed = createBeskjed("1", "1", "1", true)
        val beskjedDTO = toMaskedBeskjedDTO(beskjed)
        beskjedDTO.eventTidspunkt `should be` beskjed.eventTidspunkt
        beskjedDTO.eventId `should be equal to` beskjed.eventId
        beskjedDTO.tekst `should be equal to` "***"
        beskjedDTO.link `should be equal to` "***"
        beskjedDTO.sistOppdatert `should be` beskjed.sistOppdatert
        beskjedDTO.sikkerhetsnivaa `should be` beskjed.sikkerhetsnivaa
    }
}
