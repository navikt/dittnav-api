package no.nav.personbruker.dittnav.api.event

data class Event(
    val aktiv: Boolean,
    val aktorId: String,
    val dokumentId: String,
    val eventId: String,
    val eventTidspunkt: String,
    val id: String,
    val link: String,
    val produsent: String,
    val sikkerhetsnivaa: Int,
    val sistOppdatert: String,
    val tekst: String
)
