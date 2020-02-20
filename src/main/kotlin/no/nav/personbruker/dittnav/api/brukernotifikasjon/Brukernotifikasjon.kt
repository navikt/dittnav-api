package no.nav.personbruker.dittnav.api.brukernotifikasjon

import java.time.ZonedDateTime

data class Brukernotifikasjon(
        val uid: Int?,
        val produsent: String,
        val eventTidspunkt: ZonedDateTime,
        val eventId: String,
        val tekst: String,
        val link: String,
        val sistOppdatert: ZonedDateTime,
        val type: BrukernotifikasjonType
)
