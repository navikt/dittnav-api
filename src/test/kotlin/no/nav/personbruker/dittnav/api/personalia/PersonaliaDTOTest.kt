package no.nav.personbruker.dittnav.api.personalia

import io.kotest.matchers.nulls.shouldNotBeNull
import kotlinx.serialization.decodeFromString
import no.nav.personbruker.dittnav.api.config.json
import org.junit.jupiter.api.Test

internal class PersonaliaDTOTest {


    @Test
    fun `Skal kunne deserialisere responsen fra mine saker med navn`() {
        val response = """
        {
          "navn": "TestName"
        }
        """.trimIndent()

        val objectMapper = json()
        val deserialized = objectMapper.decodeFromString<PersonaliaNavnDTO>(response)

        deserialized.shouldNotBeNull()
    }


    @Test
    fun `Skal kunne deserialisere responsen fra mine saker med ident`() {
        val response = """
        {
          "ident": "1234"
        }
    """.trimIndent()

        val objectMapper = json()
        val deserialized = objectMapper.decodeFromString<PersonaliaIdentDTO>(response)

        deserialized.shouldNotBeNull()
    }


}
