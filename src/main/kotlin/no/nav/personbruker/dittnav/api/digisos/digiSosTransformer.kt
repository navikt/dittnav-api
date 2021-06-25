package no.nav.personbruker.dittnav.api.digisos

import no.nav.personbruker.dittnav.api.beskjed.BeskjedDTO
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*

fun Paabegynte.toInternal() = BeskjedDTO(
            uid = UUID.randomUUID().toString(),
            eventTidspunkt = tidspunkt.toZonedDateTime(),
            eventId = grupperingsId,
            tekst = cropTextIfOverMaxLengthOfBeskjed(tekst),
            link = link,
            produsent = "digiSos",
            sistOppdatert = tidspunkt.toZonedDateTime(),
            sikkerhetsnivaa = 3,
            aktiv = synligFremTil.isAfter(LocalDateTime.now())
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
