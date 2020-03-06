package no.nav.personbruker.dittnav.api.oppgave

import com.fasterxml.jackson.annotation.JsonInclude
import java.time.ZonedDateTime

data class OppgaveDTO(
        @JsonInclude(JsonInclude.Include.NON_NULL) val uid: String?,
        val eventTidspunkt: ZonedDateTime,
        val eventId: String,
        val tekst: String,
        val link: String,
        val sistOppdatert: ZonedDateTime,
        val sikkerhetsnivaa: Int
) {
    constructor(
            eventTidspunkt: ZonedDateTime,
            eventId: String,
            tekst: String,
            link: String,
            sistOppdatert: ZonedDateTime,
            sikkerhetsnivaa: Int
    ) : this(
            null,
            eventTidspunkt,
            eventId,
            tekst,
            link,
            sistOppdatert,
            sikkerhetsnivaa
    )
}
