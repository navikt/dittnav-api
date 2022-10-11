@file:UseSerializers(LocalDateTimeSerializer::class)

package no.nav.personbruker.dittnav.api.digisos

import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import no.nav.personbruker.dittnav.api.beskjed.BeskjedDTO
import no.nav.personbruker.dittnav.api.common.serializer.LocalDateTimeSerializer
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime

@Serializable
data class Paabegynte(
    val eventTidspunkt: LocalDateTime,
    val eventId: String,
    val grupperingsId: String,
    val tekst: String,
    val link: String,
    val sikkerhetsnivaa: Int,
    val sistOppdatert: LocalDateTime,
    val isAktiv: Boolean
) {
    companion object {
        const val maxBeskjedTextLength = 150
    }

    fun toInternal() = BeskjedDTO(
        forstBehandlet = eventTidspunkt.toZonedDateTime(),
        eventId = eventId,
        tekst = cropTextIfOverMaxLengthOfBeskjed(tekst),
        link = link,
        produsent = "digiSos",
        sistOppdatert = sistOppdatert.toZonedDateTime(),
        sikkerhetsnivaa = 3,
        aktiv = isAktiv,
        grupperingsId = grupperingsId,
        eksternVarslingSendt = false,
        eksternVarslingKanaler = emptyList()
    )

    private fun cropTextIfOverMaxLengthOfBeskjed(text: String): String {
        return if (text.length <= maxBeskjedTextLength) {
            text
        } else {
            text.substring(0, maxBeskjedTextLength - 3) + "..."
        }
    }

    private fun LocalDateTime.toZonedDateTime(): ZonedDateTime {
        val zone = ZoneId.of("Europe/Oslo")
        return ZonedDateTime.of(this, zone)
    }
}

@Serializable
data class DoneDTO(
    val eventId: String,
    val grupperingsId: String
)

fun List<Paabegynte>.toInternals(): List<BeskjedDTO> {
    return map { external ->
        external.toInternal()
    }
}


