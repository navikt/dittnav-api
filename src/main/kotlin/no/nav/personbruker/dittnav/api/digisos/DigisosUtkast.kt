@file:UseSerializers(LocalDateTimeSerializer::class)
package no.nav.personbruker.dittnav.api.digisos

import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import no.nav.personbruker.dittnav.api.common.serializer.LocalDateTimeSerializer
import java.time.LocalDateTime

@Serializable
class DigisosUtkast(
    val utkastId: String,
    val tittel: String,
    val link: String,
    val opprettet: LocalDateTime
)
