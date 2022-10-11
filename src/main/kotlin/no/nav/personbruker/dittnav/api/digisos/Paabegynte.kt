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
    val eventId : String,
    val grupperingsId : String,
    val tekst: String,
    val link: String,
    val sikkerhetsnivaa: Int,
    val sistOppdatert: LocalDateTime,
    val isAktiv : Boolean
)

fun List<Paabegynte>.toInternals(): List<BeskjedDTO> {
    return map { external ->
        external.toInternal()
    }
}

fun Paabegynte.toInternal() = BeskjedDTO(
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

const val maxBeskjedTextLength = 150

private fun cropTextIfOverMaxLengthOfBeskjed(text: String): String {
    return if (text.length <= maxBeskjedTextLength) {
        text

    } else {
        text.substring(0, maxBeskjedTextLength - 3) + "..."
    }
}

fun LocalDateTime.toZonedDateTime(): ZonedDateTime {
    val zone = ZoneId.of("Europe/Oslo")
    return ZonedDateTime.of(this, zone)
}

