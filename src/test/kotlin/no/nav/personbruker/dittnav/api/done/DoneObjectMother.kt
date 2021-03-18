package no.nav.personbruker.dittnav.api.done

object DoneObjectMother {

    fun createDone(uid: String, eventId: String): DoneDTO {
        return DoneDTO(
                uid = uid,
                eventId = eventId)
    }

}
