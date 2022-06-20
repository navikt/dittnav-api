package no.nav.personbruker.dittnav.api.oppgave

import java.time.ZonedDateTime
import java.time.ZoneId

fun createOppgave(eventId: String, fodselsnummer: String, aktiv: Boolean): Oppgave {
    return Oppgave(
            forstBehandlet = ZonedDateTime.now(ZoneId.of("Europe/Oslo")),
            fodselsnummer = fodselsnummer,
            eventId = eventId,
            grupperingsId = "Dok123",
            tekst = "Dette er en oppgave til brukeren",
            link = "https://nav.no/systemX/",
            produsent = "dittnav",
            sikkerhetsnivaa = 4,
            sistOppdatert = ZonedDateTime.now(ZoneId.of("Europe/Oslo")),
            aktiv = aktiv
    )
}
