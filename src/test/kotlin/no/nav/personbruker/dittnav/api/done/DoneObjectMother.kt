package no.nav.personbruker.dittnav.api.done

object DoneObjectMother {

    fun createDone(uid: String, eventId: String): DoneDto {
        return DoneDto(
                uid = uid,
                eventId = eventId)
    }
}