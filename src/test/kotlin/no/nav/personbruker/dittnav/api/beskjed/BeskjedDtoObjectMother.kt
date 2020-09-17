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
            true
        )
    }

    fun createNumberOfActiveBeskjed(number: Int, baseEventId: String = "beskjed"): List<BeskjedDTO> {
        val list = mutableListOf<BeskjedDTO>()
        for (i in 1..number) {
            list.add(createActiveBeskjed("$baseEventId-$i"))
        }
        return list
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
            false
        )
    }

    fun createNumberOfInactiveBeskjed(number: Int, baseEventId: String = "beskjed"): List<BeskjedDTO> {
        val list = mutableListOf<BeskjedDTO>()
        for (i in 1..number) {
            list.add(createInactiveBeskjed("$baseEventId-$i"))
        }
        return list
    }

}
