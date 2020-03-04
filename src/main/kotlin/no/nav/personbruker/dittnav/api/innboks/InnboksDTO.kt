package no.nav.personbruker.dittnav.api.innboks

import java.time.ZonedDateTime

data class InnboksDTO(
        val produsent: String,
        val eventTidspunkt: ZonedDateTime,
        val eventId: String,
        val tekst: String,
        val link: String,
        val sistOppdatert: ZonedDateTime
)
