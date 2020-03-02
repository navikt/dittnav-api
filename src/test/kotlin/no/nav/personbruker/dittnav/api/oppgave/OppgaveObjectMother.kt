package no.nav.personbruker.dittnav.api.oppgave

import java.time.ZoneId
import java.time.ZonedDateTime

object OppgaveObjectMother {

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
                aktiv = true,
                produsent = "enSystembruker"
        )
    }
}
