@file:UseSerializers(ZonedDateTimeSerializer::class, URLSerializer::class)

package no.nav.personbruker.dittnav.api.saker

import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import no.nav.personbruker.dittnav.api.common.serializer.URLSerializer
import no.nav.personbruker.dittnav.api.common.serializer.ZonedDateTimeSerializer
import java.time.ZonedDateTime

@Serializable
data class SisteSakstemaer(
    val sistEndrede: List<Sakstema>,
    val dagpengerSistEndret: ZonedDateTime?
) {
    fun toInternal(): SisteSakstemaerDTO =
        SisteSakstemaerDTO(
            sistEndrede.toInternal(),
            dagpengerSistEndret
        )
}

@Serializable
data class SisteSakstemaerDTO(
    val sakstemaer: List<SakstemaDTO>,
    val dagpengerSistEndret: ZonedDateTime?
)

private fun List<Sakstema>.toInternal(): List<SakstemaDTO> {
    return filterNotNull().map { external ->
        external.toInternal()
    }.toList()
}