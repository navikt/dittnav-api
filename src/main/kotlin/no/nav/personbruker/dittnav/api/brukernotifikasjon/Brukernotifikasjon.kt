package no.nav.personbruker.dittnav.api.brukernotifikasjon

import no.nav.personbruker.dittnav.api.config.EventType
import java.time.LocalDateTime

data class Brukernotifikasjon(
    val produsent: String,
    val eventTidspunkt: LocalDateTime,
    val eventId: String,
    val tekst: String,
    val link: String,
    val sistOppdatert: LocalDateTime,
    val type: EventType
)
