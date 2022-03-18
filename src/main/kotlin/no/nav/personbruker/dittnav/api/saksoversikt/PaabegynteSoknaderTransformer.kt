package no.nav.personbruker.dittnav.api.saksoversikt

import no.nav.personbruker.dittnav.api.saksoversikt.external.PaabegynteSoknaderExternal
import no.nav.personbruker.dittnav.api.saksoversikt.external.UriWrapper

class PaabegynteSoknaderTransformer(private val saksoversiktUrl: String) {

    fun toInternal(external: PaabegynteSoknaderExternal): PaabegynteSoknader {
        val url = determineUrl(external.paabegynte)
        val antall = external.paabegynte.size

        val baksystem = external.feilendeBaksystem.map { it.baksystem }

        return PaabegynteSoknader(url, antall, baksystem)
    }

    private fun determineUrl(paabegynte: List<UriWrapper>): String? {
        return when {
            paabegynte.isEmpty() -> null
            paabegynte.size == 1 -> paabegynte.first().uri
            else -> saksoversiktUrl
        }
    }
}
