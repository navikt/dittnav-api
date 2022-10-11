package no.nav.personbruker.dittnav.api.oppgave

import java.time.ZonedDateTime

fun createOppgave(
    eventId: String,
    aktiv: Boolean,
    forstBehandlet: ZonedDateTime = ZonedDateTime.now().minusDays(30),
    tekst: String = "teksjhgtn lgkjhg hjkjf hljh k fjhghjgjgj",
    link: String = "http://testlink.no",
    produsent: String = "testprodusent",
    sistOppdatert: ZonedDateTime = ZonedDateTime.now().minusDays(1),
    sikkerhetsnivaa: Int = 3,
    grupperingsId: String = "gruppegrupp",
    eksternVarslingSendt: Boolean = false,
    eksternVarslingKanaler: List<String> = listOf(),
    fødselsnummer: String = "2345678909876"
) = Oppgave(
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
    eksternVarslingKanaler = eksternVarslingKanaler,
    fodselsnummer = fødselsnummer
)