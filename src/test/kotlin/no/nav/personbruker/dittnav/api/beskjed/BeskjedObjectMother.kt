package no.nav.personbruker.dittnav.api.beskjed

import java.time.ZoneId
import java.time.ZonedDateTime

fun createBeskjed(eventId: String, fodselsnummer: String, aktiv: Boolean): Beskjed {
    return Beskjed(
            eventTidspunkt = ZonedDateTime.now(ZoneId.of("Europe/Oslo")),
            fodselsnummer = fodselsnummer,
            eventId = eventId,
            grupperingsId = "Dok123",
            tekst = "Dette er beskjed til brukeren",
            link = "https://nav.no/systemX/",
            produsent = "dittnav",
            sikkerhetsnivaa = 4,
            sistOppdatert = ZonedDateTime.now(ZoneId.of("Europe/Oslo")),
            aktiv = aktiv
    )
}
