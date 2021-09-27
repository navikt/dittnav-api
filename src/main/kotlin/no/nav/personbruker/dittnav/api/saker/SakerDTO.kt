@file:UseSerializers(ZonedDateTimeSerializer::class, URLSerializer::class)
package no.nav.personbruker.dittnav.api.saker

import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import no.nav.personbruker.dittnav.api.common.serializer.URLSerializer
import no.nav.personbruker.dittnav.api.common.serializer.ZonedDateTimeSerializer
import java.net.URL
import java.time.ZonedDateTime

@Serializable
data class SakerDTO(
        val navn: String,
        val kode: String,
        val sistEndret: ZonedDateTime,
        val detaljvisningUrl : URL
)
