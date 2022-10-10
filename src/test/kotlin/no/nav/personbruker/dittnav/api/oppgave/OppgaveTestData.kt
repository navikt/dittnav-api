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
        aktiv = aktiv,
        eksternVarslingSendt = true,
        eksternVarslingKanaler = listOf("SMS", "EPOST")
    )
}

fun createOppgaveDTO(
    eventId: String,
    aktiv: Boolean,
    forstBehandlet: ZonedDateTime = ZonedDateTime.now().minusDays(30),
    tekst: String = "teksjhgtn lgkjhg hjkjf hljh k fjhghjgjgj",
    link: String = "http://testlink.no",
    produsent: String = "testprodusent",
    sistOppdatert: ZonedDateTime = ZonedDateTime.now().minusDays(1),
    sikkerhetsnivaa: Int = 3,
    grupperingsId:String = "gruppegrupp",
    eksternVarslingSendt: Boolean = false,
    eksternVarslingKanaler: List<String> = listOf()
) = OppgaveDTO(
    forstBehandlet = forstBehandlet,
    eventId = eventId,
    tekst = tekst,
    link = link,
    produsent = produsent,
    sistOppdatert = sistOppdatert,
    sikkerhetsnivaa = sikkerhetsnivaa,
    aktiv = aktiv,
    grupperingsId = grupperingsId,
    eksternVarslingSendt = eksternVarslingSendt,
    eksternVarslingKanaler = eksternVarslingKanaler
)