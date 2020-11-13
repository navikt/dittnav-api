package no.nav.personbruker.dittnav.api.loginstatus

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class InnloggingsstatusResponse (
        val authenticated: Boolean,
        val authLevel : Int? = null
)