package no.nav.personbruker.dittnav.api.beskjed

import java.time.ZonedDateTime

object BeskjedDtoObjectMother {

    fun createActiveBeskjed(eventId: String): BeskjedDTO {
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

    fun createInactiveBeskjed(eventId: String): BeskjedDTO {
        return BeskjedDTO(
            forstBehandlet = ZonedDateTime.now(),
            eventId = eventId,
            tekst = "Dummytekst",
            link = "https://dummy.url",
            produsent = "dummy-produsent",
            sistOppdatert = ZonedDateTime.now().minusDays(1),
            sikkerhetsnivaa = 3,
            aktiv = false,
            grupperingsId = "654",
            eksternVarslingSendt = true,
            eksternVarslingKanaler = listOf("SMS", "EPOST")
        )
    }

}
