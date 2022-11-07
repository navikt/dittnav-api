package no.nav.personbruker.dittnav.api.personalia

import kotlinx.serialization.Serializable

@Serializable
data class NavnDTO(
    val navn: String?
)

@Serializable
data class IdentDTO(
    val ident: String
)
