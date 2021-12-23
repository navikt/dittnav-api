@file:UseSerializers(LocalDateTimeSerializer::class)
package no.nav.personbruker.dittnav.api.digisos

import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import no.nav.personbruker.dittnav.api.common.serializer.LocalDateTimeSerializer
import java.time.LocalDateTime

@Serializable
data class Paabegynte(
    val eventTidspunkt: LocalDateTime,
    val eventId : String,
    val grupperingsId : String,
    val tekst: String,
    val link: String,
    val sikkerhetsnivaa: Int,
    val sistOppdatert: LocalDateTime,
    val isAktiv : Boolean
)
