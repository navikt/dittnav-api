package no.nav.personbruker.dittnav.api.beskjed

import java.time.ZonedDateTime

data class Beskjed(
        val uid: String,
        val eventTidspunkt: ZonedDateTime,
        val fodselsnummer: String,
        val eventId: String,
        val grupperingsId: String,
        val tekst: String,
        val link: String,
        val produsent: String,
        val sikkerhetsnivaa: Int,
        val sistOppdatert: ZonedDateTime,
        val aktiv: Boolean
) {
    override fun toString(): String {
        return "Beskjed(" +
                "uid=$uid, " +
                "eventTidspunkt=$eventTidspunkt, " +
                "fodselsnummer=***, " +
                "eventId=$eventId, " +
                "grupperingsId=$grupperingsId, " +
                "tekst=***, " +
                "link=***, " +
                "produsent=$produsent, " +
                "sikkerhetsnivaa=$sikkerhetsnivaa, " +
                "sistOppdatert=$sistOppdatert, " +
                "aktiv=$aktiv"
    }
}
