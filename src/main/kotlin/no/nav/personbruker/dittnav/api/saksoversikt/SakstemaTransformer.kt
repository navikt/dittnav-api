package no.nav.personbruker.dittnav.api.saksoversikt

import no.nav.personbruker.dittnav.api.saksoversikt.external.SakstemaExternal

object SakstemaTransformer {

    fun toInternal(external: List<SakstemaExternal>): SakstemaWrapper {
        val sakstemaList = mapAndSortSakstema(external)
        val antall = external.size

        return SakstemaWrapper(antall, sakstemaList)
    }

    private fun mapAndSortSakstema(external: List<SakstemaExternal>): List<Sakstema> {
        return external.map {
            Sakstema(
                temanavn = it.temanavn,
                temakode = it.temakode,
                antallStatusUnderBehandling = it.antallStatusUnderBehandling,
                sisteOppdatering = it.sisteOppdatering)
        }.sortedByDescending { it.sisteOppdatering }
    }
}
