package no.nav.personbruker.dittnav.api.oppgave

import no.nav.personbruker.dittnav.api.brukernotifikasjon.BrukernotifikasjonType
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
        oppgaveDTO.sistOppdatert `should be` oppgave1.sistOppdatert
        oppgaveDTO.sikkerhetsnivaa `should be` oppgave1.sikkerhetsnivaa
    }

    @Test
    fun `should transform from Oppgave to Brukernotifikasjon`() {
        val oppgave1 = createOppgave("1", "1", true)
        val oppgave2 = createOppgave("2", "2", true)
        val brukernotifikasjonList = listOf(oppgave1, oppgave2).map { toBrukernotifikasjon(it) }
        val brukernotifikasjon = brukernotifikasjonList.first()

        brukernotifikasjon.eventTidspunkt `should be` oppgave1.eventTidspunkt
        brukernotifikasjon.eventId `should be equal to` oppgave1.eventId
        brukernotifikasjon.tekst `should be equal to` oppgave1.tekst
        brukernotifikasjon.link `should be equal to` oppgave1.link
        brukernotifikasjon.sistOppdatert `should be` oppgave1.sistOppdatert
        brukernotifikasjon.type `should be` BrukernotifikasjonType.OPPGAVE
    }
}
