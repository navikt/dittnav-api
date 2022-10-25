package no.nav.personbruker.dittnav.api.innboks

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class InnboksTest {

    @Test
    fun `should transform from Innboks to InnboksDTO`() {
        val innboks1 = createInnboks(eventId = "1", fodselsnummer = "1", aktiv = true, sikkerhetsnivå = 4)

        innboks1.toInnboksDTO(operatingLoginLevel = 4).apply {
            forstBehandlet shouldBe innboks1.forstBehandlet
            eventId shouldBe innboks1.eventId
            tekst shouldBe innboks1.tekst
            link shouldBe innboks1.link
            produsent shouldBe innboks1.produsent
            sistOppdatert shouldBe innboks1.sistOppdatert
            sikkerhetsnivaa shouldBe innboks1.sikkerhetsnivaa
            eksternVarslingSendt shouldBe innboks1.eksternVarslingSendt
            eksternVarslingKanaler shouldBe innboks1.eksternVarslingKanaler
        }
    }

    @Test
    fun `should mask tekst, link and produsent`() {
        val innboks = createInnboks(eventId = "1", fodselsnummer = "1", aktiv = true, sikkerhetsnivå = 4)
        innboks.toInnboksDTO(operatingLoginLevel = 3).apply {
            forstBehandlet shouldBe innboks.forstBehandlet
            eventId shouldBe innboks.eventId
            tekst shouldBe "***"
            link shouldBe "***"
            produsent shouldBe "***"
            sistOppdatert shouldBe innboks.sistOppdatert
            sikkerhetsnivaa shouldBe innboks.sikkerhetsnivaa
            eksternVarslingSendt shouldBe innboks.eksternVarslingSendt
            eksternVarslingKanaler shouldBe innboks.eksternVarslingKanaler
        }

    }
}
