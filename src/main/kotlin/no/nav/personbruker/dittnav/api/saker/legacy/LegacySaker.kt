@file:UseSerializers(ZonedDateTimeSerializer::class)
package no.nav.personbruker.dittnav.api.saker.legacy

import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import no.nav.personbruker.dittnav.api.common.serializer.ZonedDateTimeSerializer

@Serializable
data class LegacySaker(
        val antallSakstema: Int,
        val sakstemaList: List<LegacySakstemaDTO>
)

