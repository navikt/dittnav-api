@file:UseSerializers(ZonedDateTimeSerializer::class, URLSerializer::class)

package no.nav.personbruker.dittnav.api.saker

import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import no.nav.personbruker.dittnav.api.common.serializer.URLSerializer
import no.nav.personbruker.dittnav.api.common.serializer.ZonedDateTimeSerializer
import java.net.URL
import java.time.ZonedDateTime

@Serializable
data class Sakstema(
    val navn: String,
    val kode: String,
    val sistEndret: ZonedDateTime,
    val detaljvisningUrl: URL
) {
    fun toInternal(): SakstemaDTO {
        return SakstemaDTO(
            navn = navn,
            kode = kode,
            sistEndret = sistEndret,
            detaljvisningUrl = detaljvisningUrl
        )
    }
}

@Serializable
data class SakstemaDTO(
    val navn: String,
    val kode: String,
    val sistEndret: ZonedDateTime,
    val detaljvisningUrl: URL
)

@Serializable
data class SakerDTO(
    val sakstemaer: List<SakstemaDTO>,
    val sakerURL: URL,
    val dagpengerSistEndret: ZonedDateTime?
)

