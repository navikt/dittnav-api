package no.nav.personbruker.dittnav.api.saksoversikt

import kotlinx.serialization.Serializable

@Serializable
data class PaabegynteSoknader(
    val url: String?,
    val antallPaabegynte: Int,
    val feilendeBaksystem: List<String>
)
