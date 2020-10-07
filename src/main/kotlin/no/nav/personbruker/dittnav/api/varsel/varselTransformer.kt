package no.nav.personbruker.dittnav.api.varsel

import no.nav.personbruker.dittnav.api.beskjed.BeskjedDTO
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime

fun toVarselDTO(externalVarsel: Varsel): BeskjedDTO =
    externalVarsel.let { varsel ->
        BeskjedDTO(
            uid = "${varsel.meldingsType}-${varsel.varselId}",
            eventTidspunkt = varsel.datoOpprettet,
            eventId = varsel.varselId,
            tekst = cropTextIfOverMaxLengthOfBeskjed(varsel.varseltekst),
            link = varsel.url,
            produsent = "varselinnboks",
            sistOppdatert = chooseSistOppdatert(varsel),
            sikkerhetsnivaa = 3,
            aktiv = varsel.datoLest == null
        )
    }

private fun chooseSistOppdatert(varsel: Varsel): ZonedDateTime {
    return varsel.datoLest ?: varsel.datoOpprettet
}

private fun cropTextIfOverMaxLengthOfBeskjed(text: String): String {
    return if (text.length <= 150) {
        text

    } else {
        text.substring(0, 150)
    }
}

fun LocalDate.toZonedDateTime(): ZonedDateTime {
    val zone = ZoneId.of("Europe/Oslo")
    val instant = atStartOfDay(zone).toInstant()
    return ZonedDateTime.ofInstant(instant, zone)
}
