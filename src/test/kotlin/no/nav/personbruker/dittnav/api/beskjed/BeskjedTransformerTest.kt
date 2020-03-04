package no.nav.personbruker.dittnav.api.beskjed

import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.`should be`
import org.junit.jupiter.api.Test

class BeskjedTransformerTest {

    @Test
    fun `should transform from Beskjed to BeskjedDTO`() {
        val beskjed1 = createBeskjed("1", "1")
        val beskjed2 = createBeskjed("2", "2")
        val beskjedDTOList = listOf(beskjed1, beskjed2).map { toBeskjedDTO(it) }
        val beskjedDTO = beskjedDTOList.first()

        beskjedDTO.produsent `should be equal to` beskjed1.produsent
        beskjedDTO.eventTidspunkt `should be` beskjed1.eventTidspunkt
        beskjedDTO.eventId `should be equal to` beskjed1.eventId
        beskjedDTO.tekst `should be equal to` beskjed1.tekst
        beskjedDTO.link `should be equal to` beskjed1.link
        beskjedDTO.sistOppdatert `should be` beskjed1.sistOppdatert
    }
}
