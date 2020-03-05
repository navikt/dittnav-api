package no.nav.personbruker.dittnav.api.brukernotifikasjon

import com.fasterxml.jackson.annotation.JsonInclude
import java.time.ZonedDateTime

data class Brukernotifikasjon(
        @JsonInclude(JsonInclude.Include.NON_NULL) val uid: String?,
        val eventTidspunkt: ZonedDateTime,
        val eventId: String,
        val tekst: String,
        val link: String,
        val sistOppdatert: ZonedDateTime,
        val type: BrukernotifikasjonType,
        val sikkerhetsnivaa: Int
) {
    constructor(
            eventTidspunkt: ZonedDateTime,
            eventId: String,
            tekst: String,
            link: String,
            sistOppdatert: ZonedDateTime,
            type: BrukernotifikasjonType,
            sikkerhetsnivaa: Int
    ) : this(
            null,
            eventTidspunkt,
            eventId,
            tekst,
            link,
            sistOppdatert,
            type,
            sikkerhetsnivaa
    )
}
