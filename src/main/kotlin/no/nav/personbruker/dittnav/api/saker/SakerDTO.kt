package no.nav.personbruker.dittnav.api.saker

import kotlinx.serialization.Serializable

@Serializable
data class SakerDTO(
        val navn: String,
        val kode: String,
        val sistEndret: String,
        val detaljvisningUrl : String
)
