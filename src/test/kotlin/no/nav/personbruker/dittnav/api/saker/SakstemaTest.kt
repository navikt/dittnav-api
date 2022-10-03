package no.nav.personbruker.dittnav.api.saker

import io.kotest.matchers.nulls.shouldNotBeNull
import kotlinx.serialization.decodeFromString
import no.nav.personbruker.dittnav.api.config.jsonConfig
import org.junit.jupiter.api.Test

internal class SakstemaTest {

    private val response = """
        {
          "navn": "Generell",
          "kode": "GEN",
          "sistEndret": "2021-09-15T10:57:56Z",
          "detaljvisningUrl": "https://person.dev.nav.no/mine-saker/tema/GEN"
        }
    """.trimIndent()

    @Test
    fun `Skal kunne deserialisere responsen fra mine saker`() {
        val objectMapper = jsonConfig()

        val deserialized = objectMapper.decodeFromString<Sakstema>(response)

        deserialized.shouldNotBeNull()
    }

}
