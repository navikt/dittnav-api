package no.nav.personbruker.dittnav.api.saksoversikt

import no.nav.personbruker.dittnav.api.saksoversikt.external.BaksystemWrapper
import no.nav.personbruker.dittnav.api.saksoversikt.external.PaabegynteSoknaderExternal
import no.nav.personbruker.dittnav.api.saksoversikt.external.UriWrapper

object PaabegynteSoknaderExternalObjectMother {
    fun createPaabegyntSoknad(paabegynteUrls: List<String>, feilendeBaksystem: List<String>): PaabegynteSoknaderExternal {
        val uriWrappers = paabegynteUrls.map { UriWrapper(it) }
        val baksystemWrappers = feilendeBaksystem.map { BaksystemWrapper(it) }

        return PaabegynteSoknaderExternal(uriWrappers, baksystemWrappers)
    }
}
