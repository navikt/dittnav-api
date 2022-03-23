package no.nav.personbruker.dittnav.api.meldekort

import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class NyeMeldekort(
    val antallNyeMeldekort: Int,
    val nesteMeldekort: Meldekort?,
    @Serializable(with = MeldekortLocalDateSerializer::class) val nesteInnsendingAvMeldekort: LocalDate?
)
