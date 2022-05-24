package no.nav.personbruker.dittnav.api.beskjed

import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.`should be`
import org.junit.jupiter.api.Test

class BeskjedTransformerTest {

    @Test
    fun `should transform from Beskjed to BeskjedDTO`() {
        val beskjed1 = createBeskjed(eventId = "1", fodselsnummer = "1",  aktiv = true)
        val beskjed2 = createBeskjed(eventId = "2", fodselsnummer = "2", aktiv = true)
        val beskjedDTOList = listOf(beskjed1, beskjed2).map { toBeskjedDTO(it) }
        val beskjedDTO = beskjedDTOList.first()

        beskjedDTO.eventTidspunkt `should be` beskjed1.eventTidspunkt
        beskjedDTO.forstBehandlet `should be` beskjed1.forstBehandlet
        beskjedDTO.eventId `should be equal to` beskjed1.eventId
        beskjedDTO.tekst `should be equal to` beskjed1.tekst
        beskjedDTO.link `should be equal to` beskjed1.link
        beskjedDTO.produsent!! `should be equal to` beskjed1.produsent!!
        beskjedDTO.sistOppdatert `should be` beskjed1.sistOppdatert
        beskjedDTO.sikkerhetsnivaa `should be` beskjed1.sikkerhetsnivaa
        beskjedDTO.grupperingsId `should be` beskjed1.grupperingsId
    }

    @Test
    fun `should mask tekst, link and produsent`() {
        val beskjed = createBeskjed(eventId = "1", fodselsnummer = "1", aktiv = true)
        val beskjedDTO = toMaskedBeskjedDTO(beskjed)
        beskjedDTO.eventTidspunkt `should be` beskjed.eventTidspunkt
        beskjedDTO.forstBehandlet `should be` beskjed.forstBehandlet
        beskjedDTO.eventId `should be equal to` beskjed.eventId
        beskjedDTO.tekst `should be equal to` "***"
        beskjedDTO.link `should be equal to` "***"
        beskjedDTO.produsent!! `should be equal to` "***"
        beskjedDTO.sistOppdatert `should be` beskjed.sistOppdatert
        beskjedDTO.sikkerhetsnivaa `should be` beskjed.sikkerhetsnivaa
        beskjedDTO.grupperingsId `should be` beskjed.grupperingsId
    }
}
