package no.nav.personbruker.dittnav.api.melding

import no.nav.personbruker.dittnav.api.event.EventType

data class Melding(
    val id: String,
    val type: EventType,
    val link: String,
    val sikkerhetsnivaa: Int,
    val sistOppdatert: String,
    val tekst: String
)
