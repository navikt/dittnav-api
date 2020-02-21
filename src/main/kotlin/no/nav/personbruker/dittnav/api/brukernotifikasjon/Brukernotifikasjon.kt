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
        val type: BrukernotifikasjonType
)