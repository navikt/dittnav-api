package no.nav.personbruker.dittnav.api.informasjon

import java.time.LocalDateTime
import java.time.ZoneId

object InformasjonObjectMother {

    fun createInformasjon(eventId: String, aktorId: String): Informasjon {
        return Informasjon(
                produsent = "DittNav",
                eventTidspunkt = LocalDateTime.now(ZoneId.of("Europe/Oslo")),
                aktorId = aktorId,
                eventId = eventId,
                dokumentId = "Dok123",
                tekst = "Dette er informasjon til brukeren",
                link = "https://nav.no/systemX/",
                sikkerhetsnivaa = 4,
                sistOppdatert = LocalDateTime.now(ZoneId.of("Europe/Oslo"))
        )
    }
}
