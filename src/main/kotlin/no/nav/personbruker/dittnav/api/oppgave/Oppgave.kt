package no.nav.personbruker.dittnav.api.oppgave

import java.time.ZonedDateTime

data class Oppgave(
        val eventTidspunkt: ZonedDateTime,
        val fodselsnummer: String,
        val eventId: String,
        val grupperingsId: String,
        val tekst: String,
        val link: String,
        val sikkerhetsnivaa: Int,
        val sistOppdatert: ZonedDateTime,
        val aktiv: Boolean
) {
    override fun toString(): String {
        return "Oppgave(" +
                "eventTidspunkt=$eventTidspunkt, " +
                "fodselsnummer=***, " +
                "eventId=$eventId, " +
                "grupperingsId=$grupperingsId, " +
                "tekst=***, " +
                "link=***, " +
                "sikkerhetsnivaa=$sikkerhetsnivaa, " +
                "sistOppdatert=$sistOppdatert, " +
                "aktiv=$aktiv"
    }
}
