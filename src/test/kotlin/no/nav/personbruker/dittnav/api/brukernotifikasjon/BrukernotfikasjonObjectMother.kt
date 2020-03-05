package no.nav.personbruker.dittnav.api.brukernotifikasjon

import java.time.ZoneId
import java.time.ZonedDateTime

object BrukernotfikasjonObjectMother {

    fun createBeskjedsBrukernotifikasjon(eventId: String, uid: String): Brukernotifikasjon {
        return Brukernotifikasjon(
                uid = uid,
                eventTidspunkt = ZonedDateTime.now(ZoneId.of("Europe/Oslo")),
                eventId = eventId,
                tekst = "Dette er beskjed til brukeren",
                link = "https://nav.no/systemX/",
                sistOppdatert = ZonedDateTime.now(ZoneId.of("Europe/Oslo")),
                type = BrukernotifikasjonType.BESKJED,
                sikkerhetsnivaa = 4
        )
    }

    fun createOppgaveBrukernotifikasjon(eventId: String): Brukernotifikasjon {
        return Brukernotifikasjon(
                eventTidspunkt = ZonedDateTime.now(ZoneId.of("Europe/Oslo")),
                eventId = eventId,
                tekst = "Dette er beskjed til brukeren",
                link = "https://nav.no/systemX/",
                sistOppdatert = ZonedDateTime.now(ZoneId.of("Europe/Oslo")),
                type = BrukernotifikasjonType.OPPGAVE,
                sikkerhetsnivaa = 4
        )
    }

    fun createInnboksBrukernotifikasjon(eventId: String): Brukernotifikasjon {
        return Brukernotifikasjon(
                eventTidspunkt = ZonedDateTime.now(ZoneId.of("Europe/Oslo")),
                eventId = eventId,
                tekst = "Dette er beskjed til brukeren",
                link = "https://nav.no/systemX/",
                sistOppdatert = ZonedDateTime.now(ZoneId.of("Europe/Oslo")),
                type = BrukernotifikasjonType.INNBOKS,
                sikkerhetsnivaa = 4
        )
    }
}
