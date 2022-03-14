package no.nav.personbruker.dittnav.api.mininnboks.external

import kotlinx.serialization.Serializable

@Serializable
data class UbehandletMeldingExternal(
    val behandlingskjedeId: String,
    val opprettetDato: String,
    val statuser: List<String>,
    val type: String,
    val undertype: String?,
    val uri: String,
    val varselid: String?
)
