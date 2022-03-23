package no.nav.personbruker.dittnav.api.oppfolging.external

import kotlinx.serialization.Serializable

@Serializable
data class OppfolgingExternal(
    val underOppfolging: Boolean
)
