package no.nav.personbruker.dittnav.api.saksoversikt.external

import kotlinx.serialization.Serializable
import no.nav.personbruker.dittnav.api.saksoversikt.SaksoversiktLocalDateSerializer
import java.time.LocalDate

@Serializable
data class SakstemaExternal(
    val temakode: String,
    val temanavn: String,
    val antallStatusUnderBehandling: Int,
    @Serializable(with = SaksoversiktLocalDateSerializer::class) val sisteOppdatering: LocalDate
)
