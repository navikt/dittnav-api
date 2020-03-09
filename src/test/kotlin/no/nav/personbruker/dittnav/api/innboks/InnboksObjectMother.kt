package no.nav.personbruker.dittnav.api.innboks

import java.time.ZoneId
import java.time.ZonedDateTime

fun createInnboks(eventId: String, fodselsnummer: String, aktiv: Boolean): Innboks {
    return Innboks(
            eventTidspunkt = ZonedDateTime.now(ZoneId.of("Europe/Oslo")),
            fodselsnummer = fodselsnummer,
            eventId = eventId,
            grupperingsId = "Dok123",
            tekst = "Dette er en oppgave til brukeren",
            link = "https://nav.no/systemX/",
            sikkerhetsnivaa = 4,
            sistOppdatert = ZonedDateTime.now(ZoneId.of("Europe/Oslo")),
            aktiv = aktiv
    )
}

fun createInnboksDTO(eventId: String): InnboksDTO {
    return InnboksDTO(
            eventTidspunkt = ZonedDateTime.now(ZoneId.of("Europe/Oslo")),
            eventId = eventId,
            tekst = "Dette er beskjed til brukeren",
            link = "https://nav.no/systemX/",
            sistOppdatert = ZonedDateTime.now(ZoneId.of("Europe/Oslo")),
            sikkerhetsnivaa = 4
    )
}
