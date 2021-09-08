package no.nav.personbruker.dittnav.api.oppgave

import java.time.ZonedDateTime

object OppgaveDtoObjectMother {

    fun createActiveOppgave(eventId: String): OppgaveDTO {
        return OppgaveDTO(
            ZonedDateTime.now().minusDays(3),
            eventId,
            "Dummytekst",
            "https://dummy.url",
            "dummy-produsent",
            ZonedDateTime.now().minusDays(3),
            3,
            true,
            "321"
        )
    }

    fun createInactiveOppgave(eventId: String): OppgaveDTO {
        return OppgaveDTO(
            ZonedDateTime.now().minusDays(3),
            eventId,
            "Dummytekst",
            "https://dummy.url",
            "dummy-produsent",
            ZonedDateTime.now().minusDays(1),
            3,
            false,
            "654"
        )
    }

}
