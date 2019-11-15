package no.nav.personbruker.dittnav.api.brukernotifikasjon

import no.nav.personbruker.dittnav.api.informasjon.Informasjon
import java.time.LocalDateTime
import java.time.ZoneId

object BrukernotfikasjonObjectMother {

    fun createInformasjonsBrukernotifikasjon(eventId: String): Brukernotifikasjon {
        return Brukernotifikasjon(
            produsent = "DittNav",
            eventTidspunkt = LocalDateTime.now(ZoneId.of("Europe/Oslo")),
            eventId = eventId,
            tekst = "Dette er informasjon til brukeren",
            link = "https://nav.no/systemX/",
            sistOppdatert = LocalDateTime.now(ZoneId.of("Europe/Oslo")),
            type = BrukernotifikasjonType.INFORMASJON
        )
    }

    fun createOppgaveBrukernotifikasjon(eventId: String): Brukernotifikasjon {
        return Brukernotifikasjon(
            produsent = "DittNav",
            eventTidspunkt = LocalDateTime.now(ZoneId.of("Europe/Oslo")),
            eventId = eventId,
            tekst = "Dette er informasjon til brukeren",
            link = "https://nav.no/systemX/",
            sistOppdatert = LocalDateTime.now(ZoneId.of("Europe/Oslo")),
            type = BrukernotifikasjonType.OPPGAVE
        )
    }
}
