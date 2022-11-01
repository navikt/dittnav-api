package no.nav.personbruker.dittnav.api.oppgave

import io.kotest.matchers.shouldBe
import no.nav.personbruker.dittnav.api.assert
import org.junit.jupiter.api.Test

class OppgaveTest {

    @Test
    fun `should not mask events with security level equal to the current use`() {
        val externalOppgave = createOppgave(eventId = "1", fødselsnummer = "1", aktiv = true, sikkerhetsnivaa = 4)
        externalOppgave.toOppgaveDTO(4).assert {
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
    fun `should not mask events with security level lower than current user`() {
        val externalOppgave = createOppgave(eventId = "1", fødselsnummer = "1", aktiv = true, sikkerhetsnivaa = 3)
        externalOppgave.toOppgaveDTO(4).assert {
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
    fun `should mask events with security level higher than current user`() {
        val oppgave = createOppgave(eventId = "1", fødselsnummer = "1", aktiv = true, sikkerhetsnivaa = 4)
        oppgave.toOppgaveDTO(3).assert {
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
