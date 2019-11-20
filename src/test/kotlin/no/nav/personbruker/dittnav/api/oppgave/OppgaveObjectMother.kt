package no.nav.personbruker.dittnav.api.oppgave

import java.time.LocalDateTime
import java.time.ZoneId

object OppgaveObjectMother {

    fun createOppgave(eventId: String, aktorId: String): Oppgave {
        return Oppgave(
                produsent = "DittNav",
                eventTidspunkt = LocalDateTime.now(ZoneId.of("Europe/Oslo")),
                aktorId = aktorId,
                eventId = eventId,
                dokumentId = "Dok123",
                tekst = "Dette er en oppgave til brukeren",
                link = "https://nav.no/systemX/",
                sikkerhetsnivaa = 4,
                sistOppdatert = LocalDateTime.now(ZoneId.of("Europe/Oslo"))
        )
    }
}
