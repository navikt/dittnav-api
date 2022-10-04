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

fun createActiveBeskjedDto(eventId: String): BeskjedDTO {
    return BeskjedDTO(
        forstBehandlet = ZonedDateTime.now(),
        eventId = eventId,
        tekst = "Dummytekst",
        link = "https://dummy.url",
        produsent = "dummy-produsent",
        sistOppdatert = ZonedDateTime.now().minusDays(3),
        sikkerhetsnivaa = 3,
        aktiv = true,
        grupperingsId = "321",
        eksternVarslingSendt = true,
        eksternVarslingKanaler = listOf("SMS", "EPOST")
    )
}