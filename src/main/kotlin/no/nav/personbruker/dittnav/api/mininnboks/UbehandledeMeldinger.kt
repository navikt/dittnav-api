package no.nav.personbruker.dittnav.api.mininnboks

import kotlinx.serialization.Serializable

@Serializable
data class UbehandledeMeldinger(
    val type: UbehandledeMeldingerType,
    val url: String,
    val antall: Int
)
