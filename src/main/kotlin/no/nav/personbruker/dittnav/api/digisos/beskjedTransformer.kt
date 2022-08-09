package no.nav.personbruker.dittnav.api.digisos

import no.nav.personbruker.dittnav.api.beskjed.BeskjedDTO
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime

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

