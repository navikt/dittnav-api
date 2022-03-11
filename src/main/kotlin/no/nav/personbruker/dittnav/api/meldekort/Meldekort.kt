package no.nav.personbruker.dittnav.api.meldekort

import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class Meldekort(
    val uke: String,
    @Serializable(with = MeldekortLocalDateSerializer::class) val kanSendesFra: LocalDate,
    @Serializable(with = MeldekortLocalDateSerializer::class) val fra: LocalDate,
    @Serializable(with = MeldekortLocalDateSerializer::class) val til: LocalDate,
    @Serializable(with = MeldekortLocalDateSerializer::class) val sisteDatoForTrekk: LocalDate,
    val risikerTrekk: Boolean
)
