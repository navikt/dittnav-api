package no.nav.personbruker.dittnav.api.oppgave

import no.nav.personbruker.dittnav.api.config.EventType
import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.`should be`
import org.junit.jupiter.api.Test


class OppgaveTransformerTest {

    @Test
    fun `should transform from Oppgave to Brukernotifikasjon`() {
        val oppgave1 = OppgaveObjectMother.createOppgave("1", "1")
        val oppgave2 = OppgaveObjectMother.createOppgave("2", "2")
        val brukernotifikasjonList = OppgaveTransformer.toBrukernotifikasjonList(listOf(oppgave1, oppgave2))
        val brukernotifikasjon1 = brukernotifikasjonList.get(0)

        brukernotifikasjon1.produsent `should be equal to` oppgave1.produsent
        brukernotifikasjon1.eventTidspunkt `should be` oppgave1.eventTidspunkt
        brukernotifikasjon1.eventId `should be equal to` oppgave1.eventId
        brukernotifikasjon1.tekst `should be equal to` oppgave1.tekst
        brukernotifikasjon1.link `should be equal to`oppgave1.link
        brukernotifikasjon1.sistOppdatert `should be` oppgave1.sistOppdatert
        brukernotifikasjon1.type `should be` EventType.OPPGAVE
    }
}
