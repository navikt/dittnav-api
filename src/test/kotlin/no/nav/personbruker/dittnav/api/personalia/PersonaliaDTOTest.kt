package no.nav.personbruker.dittnav.api.personalia

import io.kotest.matchers.nulls.shouldNotBeNull
import kotlinx.serialization.decodeFromString
import no.nav.personbruker.dittnav.api.assert
import no.nav.personbruker.dittnav.api.config.jsonConfig
import org.junit.jupiter.api.Test

internal class PersonaliaDTOTest {


    @Test
    fun `Skal kunne deserialisere responsen fra mine saker med navn`() {
        val response = """
       
       {
          "navn": "TestName"
        }
        """.trimIndent()

        jsonConfig().decodeFromString<PersonaliaNavnDTO>(response).assert {
            shouldNotBeNull()
        }
    }


    @Test
    fun `Skal kunne deserialisere responsen fra mine saker med ident`() {
        val response = """
        {
          "ident": "1234"
        }
    """.trimIndent()

        jsonConfig().decodeFromString<PersonaliaIdentDTO>(response).assert {
            shouldNotBeNull()
        }
    }


}
