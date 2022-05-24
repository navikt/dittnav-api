package no.nav.personbruker.dittnav.api.common.serializer

import io.kotest.matchers.nulls.shouldNotBeNull
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import no.nav.personbruker.dittnav.api.config.json
import org.junit.jupiter.api.Test
import java.net.URL

internal class URLSerializerTest {

    @Test
    fun `Skal kunne serialisere og deserialisere felter av typen URL`() {
        val objectMapper = json()

        val dto = DtoMedURL(
            URL("http://dummy.url")
        )

        val serialized = objectMapper.encodeToString(dto)
        serialized.shouldNotBeNull()

        val deserialized = objectMapper.decodeFromString<DtoMedURL>(serialized)
        deserialized.shouldNotBeNull()
    }

    @Serializable
    private data class DtoMedURL(
        @Serializable(with = URLSerializer::class) private val lenke: URL
    )

}
