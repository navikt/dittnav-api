package no.nav.personbruker.dittnav.api.oppfolging

import kotlinx.serialization.Serializable

@Serializable
data class Oppfolging(
    val erBrukerUnderOppfolging: Boolean
)

@Serializable
data class OppfolgingExternal(
    val underOppfolging: Boolean
) {
    fun toInternal(): Oppfolging {
        return Oppfolging(underOppfolging)
    }
}
