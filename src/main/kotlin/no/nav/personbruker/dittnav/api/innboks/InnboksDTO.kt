@file:UseSerializers(ZonedDateTimeSerializer::class)
package no.nav.personbruker.dittnav.api.innboks

import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import no.nav.personbruker.dittnav.api.common.serializer.ZonedDateTimeSerializer
import java.time.ZonedDateTime

@Serializable
data class InnboksDTO(
        val uid: String?,
        val eventTidspunkt: ZonedDateTime,
        val eventId: String,
        val tekst: String,
        val link: String,
        val produsent: String?,
        val sistOppdatert: ZonedDateTime,
        val sikkerhetsnivaa: Int
) {
    constructor(
            eventTidspunkt: ZonedDateTime,
            eventId: String,
            tekst: String,
            link: String,
            produsent: String?,
            sistOppdatert: ZonedDateTime,
            sikkerhetsnivaa: Int
    ) : this(
            null,
            eventTidspunkt,
            eventId,
            tekst,
            link,
            produsent,
            sistOppdatert,
            sikkerhetsnivaa
    )
}
