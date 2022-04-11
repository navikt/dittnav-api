package no.nav.personbruker.dittnav.api.done

object DoneDtoObjectMother {

    fun createDoneDto(eventId: String): DoneDTO {
        return DoneDTO(
                eventId = eventId)
    }

}
