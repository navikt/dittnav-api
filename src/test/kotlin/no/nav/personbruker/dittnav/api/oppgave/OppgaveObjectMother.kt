package no.nav.personbruker.dittnav.api.oppgave

import java.time.ZonedDateTime
import java.time.ZoneId

fun createOppgave(eventId: String, fodselsnummer: String): Oppgave {
    return Oppgave(
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

fun createOppgaveDTO(eventId: String): OppgaveDTO {
    return OppgaveDTO(
            eventTidspunkt = ZonedDateTime.now(ZoneId.of("Europe/Oslo")),
            eventId = eventId,
            tekst = "Dette er beskjed til brukeren",
            link = "https://nav.no/systemX/",
            sistOppdatert = ZonedDateTime.now(ZoneId.of("Europe/Oslo"))
    )
}
