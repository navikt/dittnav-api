package no.nav.personbruker.dittnav.api.done

import kotlinx.serialization.Serializable

@Serializable
data class DoneDTO(
        val uid: String,
        val eventId: String
)
