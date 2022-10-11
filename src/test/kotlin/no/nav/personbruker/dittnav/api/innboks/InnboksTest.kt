package no.nav.personbruker.dittnav.api.innboks

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class InnboksTest {

    @Test
    fun `should transform from Innboks to InnboksDTO`() {
        val innboks1 = createInnboks(eventId = "1", fodselsnummer = "1", aktiv =  true)
        val innboks2 = createInnboks(eventId = "2", fodselsnummer =  "2", aktiv = true)
        val innboksDTOList = listOf(innboks1, innboks2).map { toInnboksDTO(it) }
        val innboksDTO = innboksDTOList.first()

        innboksDTO.forstBehandlet shouldBe innboks1.forstBehandlet
        innboksDTO.eventId shouldBe innboks1.eventId
        innboksDTO.tekst shouldBe innboks1.tekst
        innboksDTO.link shouldBe innboks1.link
        innboksDTO.produsent shouldBe innboks1.produsent
        innboksDTO.sistOppdatert shouldBe innboks1.sistOppdatert
        innboksDTO.sikkerhetsnivaa shouldBe innboks1.sikkerhetsnivaa
        innboksDTO.eksternVarslingSendt shouldBe innboks1.eksternVarslingSendt
        innboksDTO.eksternVarslingKanaler shouldBe innboks1.eksternVarslingKanaler
    }

    @Test
    fun `should mask tekst, link and produsent`() {
        val innboks = createInnboks(eventId = "1", fodselsnummer = "1", aktiv = true)
        val innboksDTO = toMaskedInnboksDTO(innboks)
        innboksDTO.forstBehandlet shouldBe innboks.forstBehandlet
        innboksDTO.eventId shouldBe innboks.eventId
        innboksDTO.tekst shouldBe "***"
        innboksDTO.link shouldBe "***"
        innboksDTO.produsent shouldBe "***"
        innboksDTO.sistOppdatert shouldBe innboks.sistOppdatert
        innboksDTO.sikkerhetsnivaa shouldBe innboks.sikkerhetsnivaa
        innboksDTO.eksternVarslingSendt shouldBe innboks.eksternVarslingSendt
        innboksDTO.eksternVarslingKanaler shouldBe innboks.eksternVarslingKanaler
    }
}
