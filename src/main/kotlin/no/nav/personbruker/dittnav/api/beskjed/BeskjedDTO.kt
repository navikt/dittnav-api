package no.nav.personbruker.dittnav.api.beskjed

import com.fasterxml.jackson.annotation.JsonInclude
import java.time.ZonedDateTime

data class BeskjedDTO(
        @JsonInclude(JsonInclude.Include.NON_NULL) val uid: String?,
        val eventTidspunkt: ZonedDateTime,
        val eventId: String,
        val tekst: String,
        val link: String,
        val produsent: String?,
        val sistOppdatert: ZonedDateTime,
        val sikkerhetsnivaa: Int,
        val aktiv: Boolean
) {
    constructor(
            eventTidspunkt: ZonedDateTime,
            eventId: String,
            tekst: String,
            link: String,
            produsent: String?,
            sistOppdatert: ZonedDateTime,
            sikkerhetsnivaa: Int,
            aktiv: Boolean
    ) : this(
            null,
            eventTidspunkt,
            eventId,
            tekst,
            link,
            produsent,
            sistOppdatert,
            sikkerhetsnivaa,
            aktiv
    )
}
