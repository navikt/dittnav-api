package no.nav.personbruker.dittnav.api.oppgave

import java.time.LocalDateTime

data class Oppgave(
        val produsent: String,
        val eventTidspunkt: LocalDateTime,
        val aktorId: String,
        val eventId: String,
        val dokumentId: String,
        val tekst: String,
        val link: String,
        val sikkerhetsnivaa: Int,
        val sistOppdatert: LocalDateTime
)

