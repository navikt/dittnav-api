package no.nav.personbruker.dittnav.api.beskjed

import java.time.ZonedDateTime
import java.time.ZoneId

fun createBeskjed(eventId: String, fodselsnummer: String): Beskjed {
    return Beskjed(
            produsent = "DittNav",
            eventTidspunkt = ZonedDateTime.now(ZoneId.of("Europe/Oslo")),
            fodselsnummer = fodselsnummer,
            eventId = eventId,
            grupperingsId = "Dok123",
            tekst = "Dette er beskjed til brukeren",
            link = "https://nav.no/systemX/",
            sikkerhetsnivaa = 4,
            sistOppdatert = ZonedDateTime.now(ZoneId.of("Europe/Oslo")),
            aktiv = true
    )
}

fun createBeskjedDTO(eventId: String): BeskjedDTO {
    return BeskjedDTO(
            produsent = "DittNav",
            eventTidspunkt = ZonedDateTime.now(ZoneId.of("Europe/Oslo")),
            eventId = eventId,
            tekst = "Dette er beskjed til brukeren",
            link = "https://nav.no/systemX/",
            sistOppdatert = ZonedDateTime.now(ZoneId.of("Europe/Oslo"))
    )
}
