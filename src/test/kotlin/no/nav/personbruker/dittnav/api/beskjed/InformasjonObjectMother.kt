package no.nav.personbruker.dittnav.api.beskjed

import java.time.ZonedDateTime
import java.time.ZoneId

object BeskjedObjectMother {

    fun createBeskjed(eventId: String, aktorId: String): Beskjed {
        return Beskjed(
                produsent = "DittNav",
                eventTidspunkt = ZonedDateTime.now(ZoneId.of("Europe/Oslo")),
                aktorId = aktorId,
                eventId = eventId,
                dokumentId = "Dok123",
                tekst = "Dette er beskjed til brukeren",
                link = "https://nav.no/systemX/",
                sikkerhetsnivaa = 4,
                sistOppdatert = ZonedDateTime.now(ZoneId.of("Europe/Oslo")),
                aktiv = true
        )
    }
}
