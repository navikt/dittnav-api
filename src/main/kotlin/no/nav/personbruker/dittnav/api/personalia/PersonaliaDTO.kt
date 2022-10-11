package no.nav.personbruker.dittnav.api.personalia

import kotlinx.serialization.Serializable

@Serializable
data class PersonaliaNavnDTO(
    val navn: String
)

@Serializable
data class PersonaliaIdentDTO(
    val ident: String
)
