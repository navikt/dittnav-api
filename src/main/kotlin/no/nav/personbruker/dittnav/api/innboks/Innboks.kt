package no.nav.personbruker.dittnav.api.innboks

import java.time.ZonedDateTime

data class Innboks(
        val produsent: String,
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
