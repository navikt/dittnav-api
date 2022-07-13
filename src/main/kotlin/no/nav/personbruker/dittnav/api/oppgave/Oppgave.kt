@file:UseSerializers(ZonedDateTimeSerializer::class)
package no.nav.personbruker.dittnav.api.oppgave

import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import no.nav.personbruker.dittnav.api.common.serializer.ZonedDateTimeSerializer
import java.time.ZonedDateTime

@Serializable
data class Oppgave(
        val forstBehandlet: ZonedDateTime,
        val fodselsnummer: String,
        val eventId: String,
        val grupperingsId: String,
        val tekst: String,
        val link: String,
        val produsent: String?,
        val sikkerhetsnivaa: Int,
        val sistOppdatert: ZonedDateTime,
        val aktiv: Boolean,
        val eksternVarslingSendt: Boolean,
        val eksternVarslingKanaler: List<String>
) {
    override fun toString(): String {
        return "Oppgave(" +
                "forstBehandlet=$forstBehandlet, " +
                "fodselsnummer=***, " +
                "eventId=$eventId, " +
                "grupperingsId=$grupperingsId, " +
                "tekst=***, " +
                "link=***, " +
                "produsent=$produsent, " +
                "sikkerhetsnivaa=$sikkerhetsnivaa, " +
                "sistOppdatert=$sistOppdatert, " +
                "aktiv=$aktiv, " +
                "eksternVarslingSendt=$eksternVarslingSendt, " +
                "eksternVarslingKanaler=$eksternVarslingKanaler"
    }
}
