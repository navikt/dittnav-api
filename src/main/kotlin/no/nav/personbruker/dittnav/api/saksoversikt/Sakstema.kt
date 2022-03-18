package no.nav.personbruker.dittnav.api.saksoversikt

import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class Sakstema(
    val temanavn: String,
    val temakode: String,
    val antallStatusUnderBehandling: Int,
    @Serializable(with = SaksoversiktLocalDateSerializer::class) val sisteOppdatering: LocalDate,
)
