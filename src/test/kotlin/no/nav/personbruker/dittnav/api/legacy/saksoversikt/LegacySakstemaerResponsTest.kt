package no.nav.personbruker.dittnav.api.legacy.saksoversikt

import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.jwk.KeyUse
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator
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

    @Test
    fun `Skal kunne deserialisere responsen fra legacy`() {
        val objectMapper = json()

        val deserialized = objectMapper.decodeFromString<LegacySakstemaerRespons>(legacyActualResponse)

        deserialized.shouldNotBeNull()
    }

    internal object JwkBuilder {
        fun generateJwk(): String {
            return RSAKeyGenerator(2048)
                .keyUse(KeyUse.SIGNATURE)
                .algorithm(JWSAlgorithm.RS256)
                .keyID("KID")
                .generate()
                .toJSONString()
        }
    }

    @Test
    fun lageJwt() {

        val token = JwkBuilder.generateJwk()

        println("### ${token}")

    }

}
