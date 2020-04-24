package no.nav.personbruker.dittnav.api.oppgave

import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.`should be`
import org.junit.jupiter.api.Test

class OppgaveTransformerTest {

    @Test
    fun `should transform from Oppgave to OppgaveDTO`() {
        val oppgave1 = createOppgave("1", "1", true)
        val oppgave2 = createOppgave("2", "2", true)
        val oppgaveDTOList = listOf(oppgave1, oppgave2).map { toOppgaveDTO(it) }
        val oppgaveDTO = oppgaveDTOList.first()

        oppgaveDTO.eventTidspunkt `should be` oppgave1.eventTidspunkt
        oppgaveDTO.eventId `should be equal to` oppgave1.eventId
        oppgaveDTO.tekst `should be equal to` oppgave1.tekst
        oppgaveDTO.link `should be equal to` oppgave1.link
        oppgaveDTO.produsent!! `should be equal to` oppgave1.produsent!!
        oppgaveDTO.sistOppdatert `should be` oppgave1.sistOppdatert
        oppgaveDTO.sikkerhetsnivaa `should be` oppgave1.sikkerhetsnivaa
    }

    @Test
    fun `should mask tekst, link and produsent`() {
        val oppgave = createOppgave("1", "1", true)
        val oppgaveDTO = toMaskedOppgaveDTO(oppgave)
        oppgaveDTO.eventTidspunkt `should be` oppgave.eventTidspunkt
        oppgaveDTO.eventId `should be equal to` oppgave.eventId
        oppgaveDTO.tekst `should be equal to` "***"
        oppgaveDTO.link `should be equal to` "***"
        oppgaveDTO.produsent!! `should be equal to` "***"
        oppgaveDTO.sistOppdatert `should be` oppgave.sistOppdatert
        oppgaveDTO.sikkerhetsnivaa `should be` oppgave.sikkerhetsnivaa
    }
}
