package no.nav.personbruker.dittnav.api.legacy.saksoversikt

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import no.nav.personbruker.dittnav.api.config.json
import org.amshove.kluent.shouldNotBeNull
import org.junit.jupiter.api.Test

internal class LegacySakstemaTest {

    val responsHvorSisteOppdateringErNull = """
        {
          "temanavn": "Dagpenger",
          "temakode": "DAG",
          "antallStatusUnderBehandling": 1,
          "sisteOppdatering": null
        }
    """.trimIndent()

    @Test
    fun `Skal klare aa serialisere og deserialisere gyldige responser`() {
        val original = LegacySakstemaObjectMother.giveMeSakstemaDagpenger()

        val serialized = json().encodeToString(original)
        val deserialized = json().decodeFromString<LegacySakstema>(serialized)

        deserialized.shouldNotBeNull()
    }

    @Test
    fun `Skal taale respons med ulogiske data`() {
        val deserialized = json().decodeFromString<LegacySakstema>(responsHvorSisteOppdateringErNull)

        deserialized.shouldNotBeNull()
    }

}
