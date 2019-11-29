package no.nav.personbruker.dittnav.api.innboks

import java.time.ZoneId
import java.time.ZonedDateTime

fun createInnboks(eventId: String, fodselsnummer: String): Innboks {
    return Innboks(
            produsent = "DittNav",
            eventTidspunkt = ZonedDateTime.now(ZoneId.of("Europe/Oslo")),
            fodselsnummer = fodselsnummer,
            eventId = eventId,
            grupperingsId = "Dok123",
            tekst = "Dette er en oppgave til brukeren",
            link = "https://nav.no/systemX/",
            sikkerhetsnivaa = 4,
            sistOppdatert = ZonedDateTime.now(ZoneId.of("Europe/Oslo")),
            aktiv = true
    )
}