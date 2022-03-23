package no.nav.personbruker.dittnav.api.oppfolging

import kotlinx.serialization.Serializable

@Serializable
data class Oppfolging(
    val erBrukerUnderOppfolging: Boolean
)
