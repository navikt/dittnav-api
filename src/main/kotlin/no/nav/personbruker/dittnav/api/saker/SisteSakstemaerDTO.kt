@file:UseSerializers(URLSerializer::class, ZonedDateTimeSerializer::class)
package no.nav.personbruker.dittnav.api.saker

import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import no.nav.personbruker.dittnav.api.common.serializer.URLSerializer
import no.nav.personbruker.dittnav.api.common.serializer.ZonedDateTimeSerializer
import java.time.ZonedDateTime

@Serializable
data class SisteSakstemaerDTO(
    val sakstemaer : List<SakstemaDTO>,
    val dagpengerSistEndret : ZonedDateTime?
)
