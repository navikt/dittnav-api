package no.nav.personbruker.dittnav.api.digisos

import no.nav.personbruker.dittnav.api.beskjed.BeskjedDTO
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*

fun Paabegynte.toInternal() = BeskjedDTO(
            uid = grupperingsId,
            eventTidspunkt = eventTidspunkt.toZonedDateTime(),
            eventId = eventId,
            tekst = cropTextIfOverMaxLengthOfBeskjed(tekst),
            link = link,
            produsent = "digiSos",
            sistOppdatert = sistOppdatert.toZonedDateTime(),
            sikkerhetsnivaa = 3,
            aktiv = aktiv
        )

private fun cropTextIfOverMaxLengthOfBeskjed(text: String): String {
    return if (text.length <= 150) {
        text

    } else {
        text.substring(0, 150)
    }
}

fun LocalDateTime.toZonedDateTime(): ZonedDateTime {
    val zone = ZoneId.of("Europe/Oslo")
    return ZonedDateTime.of(this, zone)
}
