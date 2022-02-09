@file:UseSerializers(ZonedDateTimeSerializer::class, URLSerializer::class)

package no.nav.personbruker.dittnav.api.saker.ekstern

import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import no.nav.personbruker.dittnav.api.common.serializer.URLSerializer
import no.nav.personbruker.dittnav.api.common.serializer.ZonedDateTimeSerializer
import no.nav.personbruker.dittnav.api.saker.Sakstema
import java.time.ZonedDateTime

@Serializable
data class SisteSakstemaer(
    val sistEndrede: List<Sakstema>,
    val dagpengerSistEndret: ZonedDateTime?
)
