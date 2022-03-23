package no.nav.personbruker.dittnav.api.mininnboks.external

import kotlinx.serialization.Serializable

@Serializable
data class UbehandletMeldingExternal(
    val statuser: List<String> = emptyList(),
    val type: String? = null,
    val undertype: String? = null,
    val varselid: String? = null
)
