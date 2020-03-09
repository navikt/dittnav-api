package no.nav.personbruker.dittnav.api.beskjed

import java.time.ZoneId
import java.time.ZonedDateTime

fun createBeskjed(eventId: String, fodselsnummer: String, uid: String, aktiv: Boolean): Beskjed {
    return Beskjed(
            uid = uid,
            eventTidspunkt = ZonedDateTime.now(ZoneId.of("Europe/Oslo")),
            fodselsnummer = fodselsnummer,
            eventId = eventId,
            grupperingsId = "Dok123",
            tekst = "Dette er beskjed til brukeren",
            link = "https://nav.no/systemX/",
            sikkerhetsnivaa = 4,
            sistOppdatert = ZonedDateTime.now(ZoneId.of("Europe/Oslo")),
            aktiv = aktiv
    )
}

fun createBeskjedDTO(eventId: String, uid: String): BeskjedDTO {
    return BeskjedDTO(
            uid = uid,
            eventTidspunkt = ZonedDateTime.now(ZoneId.of("Europe/Oslo")),
            eventId = eventId,
            tekst = "Dette er beskjed til brukeren",
            link = "https://nav.no/systemX/",
            sistOppdatert = ZonedDateTime.now(ZoneId.of("Europe/Oslo")),
            sikkerhetsnivaa = 4
    )
}
