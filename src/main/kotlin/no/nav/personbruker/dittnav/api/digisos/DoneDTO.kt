package no.nav.personbruker.dittnav.api.digisos

import kotlinx.serialization.Serializable

@Serializable
data class DoneDTO(
        val eventId: String,
        val grupperingsId: String
)
