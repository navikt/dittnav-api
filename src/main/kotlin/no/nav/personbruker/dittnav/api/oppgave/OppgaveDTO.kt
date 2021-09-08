@file:UseSerializers(ZonedDateTimeSerializer::class)
package no.nav.personbruker.dittnav.api.oppgave

import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import no.nav.personbruker.dittnav.api.common.serializer.ZonedDateTimeSerializer
import java.time.ZonedDateTime

@Serializable
data class OppgaveDTO(
        val uid: String?,
        val eventTidspunkt: ZonedDateTime,
        val eventId: String,
        val tekst: String,
        val link: String,
        val produsent: String?,
        val sistOppdatert: ZonedDateTime,
        val sikkerhetsnivaa: Int,
        val aktiv: Boolean,
        val grupperingsId : String
) {
    constructor(
            eventTidspunkt: ZonedDateTime,
            eventId: String,
            tekst: String,
            link: String,
            produsent: String?,
            sistOppdatert: ZonedDateTime,
            sikkerhetsnivaa: Int,
            aktiv: Boolean,
            grupperingsId : String
    ) : this(
            null,
            eventTidspunkt,
            eventId,
            tekst,
            link,
            produsent,
            sistOppdatert,
            sikkerhetsnivaa,
            aktiv,
            grupperingsId
    )
}
