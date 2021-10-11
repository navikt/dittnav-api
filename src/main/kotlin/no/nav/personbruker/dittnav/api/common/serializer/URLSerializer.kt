package no.nav.personbruker.dittnav.api.common.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.net.URL

class URLSerializer : KSerializer<URL> {

    override fun deserialize(decoder: Decoder): URL {
        val value = decoder.decodeString()
        return URL(value)
    }

    override fun serialize(encoder: Encoder, value: URL) {
        encoder.encodeString(value.toString())
    }

    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("URL", PrimitiveKind.STRING)
}
