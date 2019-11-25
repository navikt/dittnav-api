package no.nav.personbruker.dittnav.api.informasjon

import java.time.ZonedDateTime

data class Informasjon(
        val produsent: String,
        val eventTidspunkt: ZonedDateTime,
        val aktorId: String,
        val eventId: String,
        val dokumentId: String,
        val tekst: String,
        val link: String,
        val sikkerhetsnivaa: Int,
        val sistOppdatert: ZonedDateTime,
        val aktiv: Boolean
)
