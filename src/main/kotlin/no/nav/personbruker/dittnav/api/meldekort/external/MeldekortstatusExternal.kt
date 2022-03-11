package no.nav.personbruker.dittnav.api.meldekort.external

import kotlinx.serialization.Serializable
import no.nav.personbruker.dittnav.api.meldekort.MeldekortLocalDateSerializer
import java.time.LocalDate

@Serializable
data class MeldekortstatusExternal(
    val meldekort: Int,
    val etterregistrerteMeldekort: Int,
    val antallGjenstaaendeFeriedager: Int,
    val nesteMeldekort: MeldekortExternal?,
    @Serializable(with = MeldekortLocalDateSerializer::class) val nesteInnsendingAvMeldekort: LocalDate?,
)
