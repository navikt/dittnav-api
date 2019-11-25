package no.nav.personbruker.dittnav.api.brukernotifikasjon

import java.time.ZoneId
import java.time.ZonedDateTime

object BrukernotfikasjonObjectMother {

    fun createInformasjonsBrukernotifikasjon(eventId: String): Brukernotifikasjon {
        return Brukernotifikasjon(
                produsent = "DittNav",
                eventTidspunkt = ZonedDateTime.now(ZoneId.of("Europe/Oslo")),
                eventId = eventId,
                tekst = "Dette er informasjon til brukeren",
                link = "https://nav.no/systemX/",
                sistOppdatert = ZonedDateTime.now(ZoneId.of("Europe/Oslo")),
                type = BrukernotifikasjonType.INFORMASJON
        )
    }

    fun createOppgaveBrukernotifikasjon(eventId: String): Brukernotifikasjon {
        return Brukernotifikasjon(
                produsent = "DittNav",
                eventTidspunkt = ZonedDateTime.now(ZoneId.of("Europe/Oslo")),
                eventId = eventId,
                tekst = "Dette er informasjon til brukeren",
                link = "https://nav.no/systemX/",
                sistOppdatert = ZonedDateTime.now(ZoneId.of("Europe/Oslo")),
                type = BrukernotifikasjonType.OPPGAVE
        )
    }
}
