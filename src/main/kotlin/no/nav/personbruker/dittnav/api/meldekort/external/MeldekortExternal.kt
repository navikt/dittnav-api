package no.nav.personbruker.dittnav.api.meldekort.external

import kotlinx.serialization.Serializable
import no.nav.personbruker.dittnav.api.meldekort.MeldekortLocalDateSerializer
import java.time.LocalDate

@Serializable
data class MeldekortExternal(
    val uke: String,
    @Serializable(with = MeldekortLocalDateSerializer::class) val kanSendesFra: LocalDate,
    @Serializable(with = MeldekortLocalDateSerializer::class) val fra: LocalDate,
    @Serializable(with = MeldekortLocalDateSerializer::class) val til: LocalDate,
)
