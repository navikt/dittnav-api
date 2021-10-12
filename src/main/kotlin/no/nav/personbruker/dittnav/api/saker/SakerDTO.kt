@file:UseSerializers(URLSerializer::class)
package no.nav.personbruker.dittnav.api.saker

import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import no.nav.personbruker.dittnav.api.common.serializer.URLSerializer
import java.net.URL

@Serializable
data class SakerDTO(
    val sakstemaer : List<SakstemaDTO>,
    val sakerURL : URL
)
