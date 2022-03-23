package no.nav.personbruker.dittnav.api.meldekort.external

import kotlinx.serialization.Serializable
import no.nav.personbruker.dittnav.api.meldekort.MeldekortLocalDateSerializer
import java.time.LocalDate

@Serializable
data class MeldekortstatusExternal(
    val meldekort: Int = 0,
    val etterregistrerteMeldekort: Int = 0,
    val antallGjenstaaendeFeriedager: Int = 0,
    val nesteMeldekort: MeldekortExternal? = null,
    @Serializable(with = MeldekortLocalDateSerializer::class) val nesteInnsendingAvMeldekort: LocalDate? = null,
)
