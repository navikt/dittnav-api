package no.nav.personbruker.dittnav.api.saker.legacy
import kotlinx.serialization.Serializable

@Serializable
data class LegacySakstemaDTO(
        val temanavn: String,
        val temakode: String,
        val antallStatusUnderBehandling: Int,
        val sisteOppdatering: String,
)

