package no.nav.personbruker.dittnav.api.beskjed

import java.time.ZoneId
import java.time.ZonedDateTime

fun createBeskjed(eventId: String, fodselsnummer: String, aktiv: Boolean): Beskjed {
    return Beskjed(
        forstBehandlet = ZonedDateTime.now(ZoneId.of("Europe/Oslo")),
        fodselsnummer = fodselsnummer,
        eventId = eventId,
        grupperingsId = "Dok123",
        tekst = "Dette er beskjed til brukeren",
        link = "https://nav.no/systemX/",
        produsent = "dittnav",
        sikkerhetsnivaa = 4,
        sistOppdatert = ZonedDateTime.now(ZoneId.of("Europe/Oslo")),
        aktiv = aktiv,
        eksternVarslingSendt = true,
        eksternVarslingKanaler = listOf("SMS", "EPOST")
    )
}

fun createActiveBeskjedDto(
    eventId: String, forstBehandlet: ZonedDateTime = ZonedDateTime.now(),
    sistOppdatert: ZonedDateTime = ZonedDateTime.now().minusDays(3),
    tekst: String = "tekst er tekst er tekst"
): BeskjedDTO =
    createBeskjedDto(
        eventId = eventId,
        aktiv = true,
        forstBehandlet = forstBehandlet,
        sistOppdatert = sistOppdatert,
        tekst = tekst
    )

fun createInactiveBeskjedDto(
    eventId: String,
    forstBehandlet: ZonedDateTime = ZonedDateTime.now(),
    sistOppdatert: ZonedDateTime = ZonedDateTime.now().minusDays(3)
): BeskjedDTO =
    createBeskjedDto(eventId = eventId, aktiv = false, forstBehandlet = forstBehandlet, sistOppdatert = sistOppdatert)

private fun createBeskjedDto(
    forstBehandlet: ZonedDateTime = ZonedDateTime.now(),
    eventId: String = "1234561",
    tekst: String = "Dummytekst",
    link: String = "https://dummy.url",
    produsent: String = "dummy-produsent",
    sistOppdatert: ZonedDateTime = ZonedDateTime.now().minusDays(3),
    sikkerhetsnivaa: Int = 3,
    aktiv: Boolean = true,
    grupperingsId: String = "321",
    eksternVarslingSendt: Boolean = false,
    eksternVarslingKanaler: List<String> = listOf()
): BeskjedDTO {
    return BeskjedDTO(
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
}