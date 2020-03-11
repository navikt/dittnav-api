package no.nav.personbruker.dittnav.api.innboks

import java.time.ZonedDateTime

data class Innboks(
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
        return "Innboks(" +
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
