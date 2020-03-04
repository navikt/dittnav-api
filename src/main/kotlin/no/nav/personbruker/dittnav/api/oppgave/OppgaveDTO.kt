package no.nav.personbruker.dittnav.api.oppgave

import java.time.ZonedDateTime

data class OppgaveDTO(
        val produsent: String,
        val eventTidspunkt: ZonedDateTime,
        val eventId: String,
        val tekst: String,
        val link: String,
        val sistOppdatert: ZonedDateTime
)
