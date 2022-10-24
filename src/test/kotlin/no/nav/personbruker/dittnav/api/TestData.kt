package no.nav.personbruker.dittnav.api


import no.nav.personbruker.dittnav.api.beskjed.Beskjed
import no.nav.personbruker.dittnav.api.beskjed.BeskjedDTO
import java.time.ZoneId
import java.time.ZonedDateTime

fun createBeskjed(
    eventId: String,
    fodselsnummer: String = "1234567890",
    aktiv: Boolean,
    forstBehandlet: ZonedDateTime = ZonedDateTime.now(ZoneId.of("Europe/Oslo")),
    grupperingsId: String = "Dok123",
    tekst: String = "Dette er beskjed til brukeren",
    link: String = "https://nav.no/systemX/",
    produsent: String = "dittnav",
    sikkerhetsnivaa: Int = 4,
    sistOppdatert: ZonedDateTime = ZonedDateTime.now(ZoneId.of("Europe/Oslo")),
    eksternVarslingSendt: Boolean = false,
    eksternVarslingKanaler: List<String> = listOf()
): Beskjed {
    return Beskjed(
        forstBehandlet = forstBehandlet,
        fodselsnummer = fodselsnummer,
        eventId = eventId,
        grupperingsId = grupperingsId,
        tekst = tekst,
        link = link,
        produsent = produsent,
        sikkerhetsnivaa = sikkerhetsnivaa,
        sistOppdatert = sistOppdatert,
        aktiv = aktiv,
        eksternVarslingSendt = eksternVarslingSendt,
        eksternVarslingKanaler = eksternVarslingKanaler
    )
}

fun createActiveBeskjed(
    eventId: String, forstBehandlet: ZonedDateTime = ZonedDateTime.now(),
    sistOppdatert: ZonedDateTime = ZonedDateTime.now().minusDays(3),
    tekst: String = "tekst er tekst er tekst",
): Beskjed =
    createBeskjed(
        eventId = eventId,
        aktiv = true,
        forstBehandlet = forstBehandlet,
        sistOppdatert = sistOppdatert,
        tekst = tekst
    )

fun createInactiveBeskjed(
    eventId: String,
    forstBehandlet: ZonedDateTime = ZonedDateTime.now(),
    sistOppdatert: ZonedDateTime = ZonedDateTime.now().minusDays(3),
): Beskjed =
    createBeskjed(
        eventId = eventId,
        aktiv = false,
        forstBehandlet = forstBehandlet,
        sistOppdatert = sistOppdatert,
    )

fun createActiveBeskjedDto(
    eventId: String,
    forstBehandlet: ZonedDateTime = ZonedDateTime.now(),
    sistOppdatert: ZonedDateTime = ZonedDateTime.now().minusDays(3),
    tekst: String = "tekst er tekst er tekst",
) = createBeskjedDto(eventId=eventId, forstBehandlet=forstBehandlet,sistOppdatert=sistOppdatert, tekst=tekst, aktiv = false)

fun createInactiveBeskjedDto(eventId: String) = createBeskjedDto(eventId=eventId)

private fun createBeskjedDto(
    eventId: String,
    forstBehandlet: ZonedDateTime = ZonedDateTime.now(),
    sistOppdatert: ZonedDateTime = ZonedDateTime.now().minusDays(3),
    tekst: String = "tekst er tekst er tekst",
    aktiv: Boolean =false
): BeskjedDTO =
    BeskjedDTO(
        eventId = eventId,
        aktiv = aktiv,
        forstBehandlet = forstBehandlet,
        sistOppdatert = sistOppdatert,
        tekst = tekst,
        link = "https://dummy.link",
        produsent = "askhfalks",
        sikkerhetsnivaa = 4,
        grupperingsId = "ahfkaj",
        eksternVarslingSendt = false,
        eksternVarslingKanaler = listOf()
    )