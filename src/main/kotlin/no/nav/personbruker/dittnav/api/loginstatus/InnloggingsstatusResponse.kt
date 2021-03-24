package no.nav.personbruker.dittnav.api.loginstatus

import kotlinx.serialization.Serializable

@Serializable
data class InnloggingsstatusResponse (
        val authenticated: Boolean,
        val authLevel : Int? = null
)
