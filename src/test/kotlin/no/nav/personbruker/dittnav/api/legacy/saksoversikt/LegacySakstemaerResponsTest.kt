package no.nav.personbruker.dittnav.api.legacy.saksoversikt

import kotlinx.serialization.decodeFromString
import no.nav.personbruker.dittnav.api.config.json
import org.amshove.kluent.shouldNotBeNull
import org.junit.jupiter.api.Test

internal class LegacySakstemaerResponsTest {

    private val legacyActualResponse = """
{
    "antallSakstema": 1,
    "sakstemaList": [
        {
            "temanavn": "Arbeidsavklaringspenger og oppfølging",
            "temakode": "AAP",
            "antallStatusUnderBehandling": 0,
            "sisteOppdatering": "2019-12-03T12:00:00+01:00"
        }
    ]
}
    """.trimIndent()

    private val objectMapper = json()

    @Test
    fun `Skal kunne deserialisere responsen fra legacy`() {

        val deserialized = objectMapper.decodeFromString<LegacySakstemaerRespons>(legacyActualResponse)

        deserialized.shouldNotBeNull()
    }

    @Test
    fun `Skal takle en tom JSON`() {
        val resp = """
            {
            "antallSakstema": 0,
            "sakstemaList": []
            }
        """.trimIndent()

        val deserialized = objectMapper.decodeFromString<LegacySakstemaerRespons>(resp)

        deserialized.shouldNotBeNull()
    }

}
