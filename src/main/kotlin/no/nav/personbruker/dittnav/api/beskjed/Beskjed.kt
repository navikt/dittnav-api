package no.nav.personbruker.dittnav.api.beskjed

import java.time.ZonedDateTime

data class Beskjed(
        val uid: String,
        val eventTidspunkt: ZonedDateTime,
        val fodselsnummer: String,
        val eventId: String,
        val grupperingsId: String,
        val tekst: String,
        val link: String,
        val sikkerhetsnivaa: Int,
        val sistOppdatert: ZonedDateTime,
        val aktiv: Boolean
)
