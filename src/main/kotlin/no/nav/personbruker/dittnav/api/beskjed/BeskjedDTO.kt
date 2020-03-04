package no.nav.personbruker.dittnav.api.beskjed

import java.time.ZonedDateTime

data class BeskjedDTO(
        val produsent: String,
        val eventTidspunkt: ZonedDateTime,
        val eventId: String,
        val tekst: String,
        val link: String,
        val sistOppdatert: ZonedDateTime
)
