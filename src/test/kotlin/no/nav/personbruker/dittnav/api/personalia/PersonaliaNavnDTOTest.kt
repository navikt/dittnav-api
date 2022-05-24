package no.nav.personbruker.dittnav.api.personalia

import io.kotest.matchers.nulls.shouldNotBeNull
import kotlinx.serialization.decodeFromString
import no.nav.personbruker.dittnav.api.config.json
import org.junit.jupiter.api.Test

internal class PersonaliaNavnDTOTest {

    private val response = """
        {
          "navn": "TestName"
        }
    """.trimIndent()

    @Test
    fun `Skal kunne deserialisere responsen fra mine saker`() {
        val objectMapper = json()
        val deserialized = objectMapper.decodeFromString<PersonaliaNavnDTO>(response)

        deserialized.shouldNotBeNull()
    }

}
