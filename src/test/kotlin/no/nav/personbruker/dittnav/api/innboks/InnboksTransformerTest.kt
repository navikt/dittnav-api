package no.nav.personbruker.dittnav.api.innboks

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class InnboksTransformerTest {

    @Test
    fun `should transform from Innboks to InnboksDTO`() {
        val innboks1 = createInnboks("1", "1", true)
        val innboks2 = createInnboks("2", "2", true)
        val innboksDTOList = listOf(innboks1, innboks2).map { toInnboksDTO(it) }
        val innboksDTO = innboksDTOList.first()

        innboksDTO.eventTidspunkt shouldBe innboks1.eventTidspunkt
        innboksDTO.forstBehandlet shouldBe innboks1.forstBehandlet
        innboksDTO.eventId shouldBe innboks1.eventId
        innboksDTO.tekst shouldBe innboks1.tekst
        innboksDTO.link shouldBe innboks1.link
        innboksDTO.produsent!! shouldBe innboks1.produsent!!
        innboksDTO.sistOppdatert shouldBe innboks1.sistOppdatert
        innboksDTO.sikkerhetsnivaa shouldBe innboks1.sikkerhetsnivaa
    }

    @Test
    fun `should mask tekst, link and produsent`() {
        val innboks = createInnboks("1", "1", true)
        val innboksDTO = toMaskedInnboksDTO(innboks)
        innboksDTO.eventTidspunkt shouldBe innboks.eventTidspunkt
        innboksDTO.forstBehandlet shouldBe innboks.forstBehandlet
        innboksDTO.eventId shouldBe innboks.eventId
        innboksDTO.tekst shouldBe "***"
        innboksDTO.link shouldBe "***"
        innboksDTO.produsent!! shouldBe "***"
        innboksDTO.sistOppdatert shouldBe innboks.sistOppdatert
        innboksDTO.sikkerhetsnivaa shouldBe innboks.sikkerhetsnivaa
    }
}
