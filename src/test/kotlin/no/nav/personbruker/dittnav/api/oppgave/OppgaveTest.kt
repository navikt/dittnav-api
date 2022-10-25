package no.nav.personbruker.dittnav.api.oppgave

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class OppgaveTest {

    @Test
    fun `should transform from Oppgave to OppgaveDTO`() {
        val externalOppgave = createOppgave(eventId = "1", fødselsnummer = "1", aktiv = true, sikkerhetsnivaa = 4)
        externalOppgave.toOppgaveDTO(4).apply {
            forstBehandlet shouldBe externalOppgave.forstBehandlet
            eventId shouldBe externalOppgave.eventId
            tekst shouldBe externalOppgave.tekst
            link shouldBe externalOppgave.link
            produsent shouldBe externalOppgave.produsent
            sistOppdatert shouldBe externalOppgave.sistOppdatert
            sikkerhetsnivaa shouldBe externalOppgave.sikkerhetsnivaa
            aktiv shouldBe externalOppgave.aktiv
            grupperingsId shouldBe externalOppgave.grupperingsId
            eksternVarslingSendt shouldBe externalOppgave.eksternVarslingSendt
            eksternVarslingKanaler shouldBe externalOppgave.eksternVarslingKanaler
        }
    }

    @Test
    fun `should mask tekst, link and produsent`() {
        val oppgave = createOppgave(eventId = "1", fødselsnummer = "1", aktiv = true)
        oppgave.toOppgaveDTO(oppgave.sikkerhetsnivaa - 1).apply {
            forstBehandlet shouldBe oppgave.forstBehandlet
            eventId shouldBe oppgave.eventId
            tekst shouldBe "***"
            link shouldBe "***"
            produsent shouldBe "***"
            sistOppdatert shouldBe oppgave.sistOppdatert
            sikkerhetsnivaa shouldBe oppgave.sikkerhetsnivaa
            eksternVarslingSendt shouldBe oppgave.eksternVarslingSendt
            eksternVarslingKanaler shouldBe oppgave.eksternVarslingKanaler
        }
    }
}
