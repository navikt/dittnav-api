package no.nav.personbruker.dittnav.api.informasjon

import no.nav.personbruker.dittnav.api.event.EventType
import java.time.LocalDateTime

data class Informasjon(
        val eventId: String,
        val produsent: String,
        val eventTidspunkt: LocalDateTime,
        val tekst: String,
        val link: String,
        val sistOppdatert: LocalDateTime,
        val type: EventType
)
