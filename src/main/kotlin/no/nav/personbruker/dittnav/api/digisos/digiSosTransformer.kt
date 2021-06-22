package no.nav.personbruker.dittnav.api.digisos

import no.nav.personbruker.dittnav.api.beskjed.BeskjedDTO
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*

fun toVarselDTO(externalVarsel: Paabegynte): BeskjedDTO =
    externalVarsel.let { varsel ->
        BeskjedDTO(
            uid = UUID.randomUUID().toString(),
            eventTidspunkt = varsel.tidspunkt.toZonedDateTime(),
            eventId = varsel.grupperingsId,
            tekst = cropTextIfOverMaxLengthOfBeskjed(varsel.tekst),
            link = varsel.link,
            produsent = "digiSos",
            sistOppdatert = varsel.tidspunkt.toZonedDateTime(),
            sikkerhetsnivaa = 3,
            aktiv = varsel.synligFremTil.isAfter(LocalDateTime.now())
        )
    }

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
