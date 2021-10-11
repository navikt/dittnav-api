@file:UseSerializers(ZonedDateTimeSerializer::class)
package no.nav.personbruker.dittnav.api.legacy.saksoversikt
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import no.nav.personbruker.dittnav.api.common.serializer.ZonedDateTimeSerializer
import java.time.ZonedDateTime

@Serializable
data class LegacySakstema(
        val temanavn: String,
        val temakode: String,
        val antallStatusUnderBehandling: Int,
        val sisteOppdatering: ZonedDateTime
)
