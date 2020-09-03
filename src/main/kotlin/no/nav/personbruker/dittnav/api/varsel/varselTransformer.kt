package no.nav.personbruker.dittnav.api.varsel

import no.nav.personbruker.dittnav.api.beskjed.BeskjedDTO
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime

fun toVarselDTO(externalVarsel: Varsel): BeskjedDTO =
    externalVarsel.let { varsel ->
        BeskjedDTO(
            uid = "${varsel.id}",
            eventTidspunkt = varsel.datoOpprettet.toZonedDateTime(),
            eventId = varsel.varselId,
            tekst = cropTextIfOverMaxLengthOfBeskjed(varsel.varseltekst),
            link = varsel.url,
            produsent = "varselinnboks",
            sistOppdatert = chooseSisteOppdatert(varsel),
            sikkerhetsnivaa = 4
        )
    }

private fun chooseSisteOppdatert(varsel: Varsel): ZonedDateTime {
    return if (varsel.datoLest != null) {
        varsel.datoLest.toZonedDateTime()

    } else {
        varsel.datoOpprettet.toZonedDateTime()
    }
}

private fun cropTextIfOverMaxLengthOfBeskjed(text: String): String {
    return if (text.length <= 150) {
        text

    } else {
        text.substring(0, 150)
    }
}

fun toMaskedVarselDTO(varsel: Varsel): BeskjedDTO =
    varsel.let {
        val maskedVarselDTO = toVarselDTO(varsel)
        return maskedVarselDTO.copy(tekst = "***", link = "***", produsent = "***")
    }

fun LocalDate.toZonedDateTime(): ZonedDateTime {
    val zone = ZoneId.of("Europe/Oslo")
    val instant = atStartOfDay(zone).toInstant()
    return ZonedDateTime.ofInstant(instant, zone)
}
