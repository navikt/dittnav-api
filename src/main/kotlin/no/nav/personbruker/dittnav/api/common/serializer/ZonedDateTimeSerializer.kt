package no.nav.personbruker.dittnav.api.common.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class ZonedDateTimeSerializer : KSerializer<ZonedDateTime> {

    override fun deserialize(decoder: Decoder): ZonedDateTime {
        val value = decoder.decodeString()
        return ZonedDateTime.parse(value)
    }

    override fun serialize(encoder: Encoder, value: ZonedDateTime) {
        encoder.encodeString(value.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))
    }

    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("ZonedDateTime", PrimitiveKind.STRING)
}
