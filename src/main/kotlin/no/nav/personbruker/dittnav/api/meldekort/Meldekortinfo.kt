package no.nav.personbruker.dittnav.api.meldekort

import kotlinx.serialization.Serializable

@Serializable
data class Meldekortinfo(
    val nyeMeldekort: NyeMeldekort,
    val resterendeFeriedager: Int,
    val etterregistrerteMeldekort: Int,
    val meldekortbruker: Boolean
)
