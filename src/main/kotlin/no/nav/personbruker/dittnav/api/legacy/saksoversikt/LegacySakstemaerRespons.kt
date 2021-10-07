@file:UseSerializers(ZonedDateTimeSerializer::class)
package no.nav.personbruker.dittnav.api.legacy.saksoversikt

import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import no.nav.personbruker.dittnav.api.common.serializer.ZonedDateTimeSerializer

@Serializable
data class LegacySakstemaerRespons(
        val antallSakstema: Int,
        val sakstemaList: List<LegacySakstema>
)

