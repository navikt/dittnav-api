package no.nav.personbruker.dittnav.api.melding

data class Melding(
    val id: String,
    val link: String,
    val sikkerhetsnivaa: Int,
    val sistOppdatert: String,
    val tekst: String
)
