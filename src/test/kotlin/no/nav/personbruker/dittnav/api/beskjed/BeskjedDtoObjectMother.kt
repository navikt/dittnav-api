package no.nav.personbruker.dittnav.api.beskjed

import java.time.ZonedDateTime

object BeskjedDtoObjectMother {

    fun createActiveBeskjed(eventId: String): BeskjedDTO {
        return BeskjedDTO(
            ZonedDateTime.now().minusDays(3),
            eventId,
            "Dummytekst",
            "https://dummy.url",
            "dummy-produsent",
            ZonedDateTime.now().minusDays(3),
            3,
            true,
            "321"
        )
    }

    fun createInactiveBeskjed(eventId: String): BeskjedDTO {
        return BeskjedDTO(
            ZonedDateTime.now().minusDays(3),
            eventId,
            "Dummytekst",
            "https://dummy.url",
            "dummy-produsent",
            ZonedDateTime.now().minusDays(1),
            3,
            false,
            "654"
        )
    }

}
