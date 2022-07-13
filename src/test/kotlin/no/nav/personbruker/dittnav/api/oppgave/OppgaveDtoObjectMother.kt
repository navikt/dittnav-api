package no.nav.personbruker.dittnav.api.oppgave

import java.time.ZonedDateTime

object OppgaveDtoObjectMother {

    fun createActiveOppgave(eventId: String): OppgaveDTO {
        return OppgaveDTO(
            forstBehandlet = ZonedDateTime.now(),
            eventId = eventId,
            tekst = "Dummytekst",
            link = "https://dummy.url",
            produsent = "dummy-produsent",
            sistOppdatert = ZonedDateTime.now().minusDays(3),
            sikkerhetsnivaa = 3,
            aktiv = true,
            grupperingsId = "321",
            eksternVarslingSendt = true,
            eksternVarslingKanaler = listOf("SMS", "EPOST")
        )
    }
}
