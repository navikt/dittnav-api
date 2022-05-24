package no.nav.personbruker.dittnav.api.beskjed

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class BeskjedTransformerTest {

    @Test
    fun `should transform from Beskjed to BeskjedDTO`() {
        val beskjed1 = createBeskjed(eventId = "1", fodselsnummer = "1",  aktiv = true)
        val beskjed2 = createBeskjed(eventId = "2", fodselsnummer = "2", aktiv = true)
        val beskjedDTOList = listOf(beskjed1, beskjed2).map { toBeskjedDTO(it) }
        val beskjedDTO = beskjedDTOList.first()

        beskjedDTO.eventTidspunkt shouldBe beskjed1.eventTidspunkt
        beskjedDTO.forstBehandlet shouldBe beskjed1.forstBehandlet
        beskjedDTO.eventId shouldBe beskjed1.eventId
        beskjedDTO.tekst shouldBe beskjed1.tekst
        beskjedDTO.link shouldBe beskjed1.link
        beskjedDTO.produsent!! shouldBe beskjed1.produsent!!
        beskjedDTO.sistOppdatert shouldBe beskjed1.sistOppdatert
        beskjedDTO.sikkerhetsnivaa shouldBe beskjed1.sikkerhetsnivaa
        beskjedDTO.grupperingsId shouldBe beskjed1.grupperingsId
    }

    @Test
    fun `should mask tekst, link and produsent`() {
        val beskjed = createBeskjed(eventId = "1", fodselsnummer = "1", aktiv = true)
        val beskjedDTO = toMaskedBeskjedDTO(beskjed)
        beskjedDTO.eventTidspunkt shouldBe beskjed.eventTidspunkt
        beskjedDTO.forstBehandlet shouldBe beskjed.forstBehandlet
        beskjedDTO.eventId shouldBe beskjed.eventId
        beskjedDTO.tekst shouldBe "***"
        beskjedDTO.link shouldBe "***"
        beskjedDTO.produsent!! shouldBe "***"
        beskjedDTO.sistOppdatert shouldBe beskjed.sistOppdatert
        beskjedDTO.sikkerhetsnivaa shouldBe beskjed.sikkerhetsnivaa
        beskjedDTO.grupperingsId shouldBe beskjed.grupperingsId
    }
}
