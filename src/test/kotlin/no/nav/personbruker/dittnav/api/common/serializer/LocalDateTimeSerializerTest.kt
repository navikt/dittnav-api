package no.nav.personbruker.dittnav.api.common.serializer

import io.kotest.matchers.shouldBe
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import no.nav.personbruker.dittnav.api.config.json
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

internal class LocalDateTimeSerializerTest {

    @Test
    fun `Serialize and deserialize`() {
        val original = SerializableClassWithLDT(LocalDateTime.now())

        val serialized = json().encodeToString(original)

        val deserialized = json().decodeFromString<SerializableClassWithLDT>(serialized)

        deserialized shouldBe original
    }

}

@Serializable
private data class SerializableClassWithLDT(
    @Serializable(with = LocalDateTimeSerializer::class) val dato: LocalDateTime
)
