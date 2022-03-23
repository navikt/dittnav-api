package no.nav.personbruker.dittnav.api.saksoversikt.external

import kotlinx.serialization.Serializable

@Serializable
data class PaabegynteSoknaderExternal(
    val paabegynte: List<UriWrapper>,
    val feilendeBaksystem: List<BaksystemWrapper>
)

@Serializable
data class UriWrapper(
    val uri: String
)

@Serializable
data class BaksystemWrapper(
    val baksystem: String
)
