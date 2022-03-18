package no.nav.personbruker.dittnav.api.saksoversikt

import kotlinx.serialization.Serializable

@Serializable
data class SakstemaWrapper(
    val antallSakstema: Int,
    val sakstemaList: List<Sakstema>
)
