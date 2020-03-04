package no.nav.personbruker.dittnav.api.oppgave

import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.`should be`
import org.junit.jupiter.api.Test

class OppgaveTransformerTest {

    @Test
    fun `should transform from Oppgave to Brukernotifikasjon`() {
        val oppgave1 = createOppgave("1", "1")
        val oppgave2 = createOppgave("2", "2")
        val brukernotifikasjonList = listOf(oppgave1, oppgave2).map { toOppgaveDTO(it) }
        val brukernotifikasjon = brukernotifikasjonList.first()

        brukernotifikasjon.produsent `should be equal to` oppgave1.produsent
        brukernotifikasjon.eventTidspunkt `should be` oppgave1.eventTidspunkt
        brukernotifikasjon.eventId `should be equal to` oppgave1.eventId
        brukernotifikasjon.tekst `should be equal to` oppgave1.tekst
        brukernotifikasjon.link `should be equal to` oppgave1.link
        brukernotifikasjon.sistOppdatert `should be` oppgave1.sistOppdatert
    }
}
