package no.nav.personbruker.dittnav.api.innboks

import no.nav.personbruker.dittnav.api.rawEventHandlerVarsel
import java.time.ZonedDateTime

fun createInnboks(
    eventId: String,
    aktiv: Boolean,
    tekst: String = "Dummy tekst som er tekst",
    link: String = "https://dummy.link",
    produsent: String = "dummyprodusent",
    sistOppdatert: ZonedDateTime = ZonedDateTime.now(),
    sikkerhetsnivå: Int = 4,
    eksternVarslingSendt: Boolean = false,
    eksternVarslingKanaler: List<String> = listOf(),
    fodselsnummer: String = "12345678918",
    grupperingsId: String = "gruppegrupp",
) = Innboks(
    forstBehandlet = ZonedDateTime.now().minusDays(30),
    aktiv = aktiv,
    eventId = eventId,
    tekst = tekst,
    link = link,
    produsent = produsent,
    sistOppdatert = sistOppdatert,
    sikkerhetsnivaa = sikkerhetsnivå,
    eksternVarslingSendt = eksternVarslingSendt,
    eksternVarslingKanaler = eksternVarslingKanaler,
    fodselsnummer = fodselsnummer,
    grupperingsId = grupperingsId
)


internal fun Innboks.toEventHandlerJson(): String = rawEventHandlerVarsel(
    eventId = eventId,
    fodselsnummer = fodselsnummer,
    grupperingsId = grupperingsId,
    førstBehandlet = "$forstBehandlet",
    produsent = produsent,
    sikkerhetsnivå = 0,
    sistOppdatert = "$sistOppdatert",
    tekst = tekst,
    link = link,
    eksternVarslingSendt = eksternVarslingSendt,
    eksternVarslingKanaler = eksternVarslingKanaler,
    aktiv = aktiv
)