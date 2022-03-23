package no.nav.personbruker.dittnav.api.saksoversikt

import kotlinx.serialization.Serializable

@Serializable
data class SakstemaWrapper(
    val antallSakstema: Int,
    val sakstemaList: List<Unit>
) {
    companion object {
        fun empty() = SakstemaWrapper(0, emptyList())
    }
}
