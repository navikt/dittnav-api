package no.nav.personbruker.dittnav.api.saker

import io.kotest.matchers.nulls.shouldNotBeNull
import kotlinx.serialization.encodeToString
import no.nav.personbruker.dittnav.api.config.jsonConfig
import org.junit.jupiter.api.Test
import java.net.URL
import java.time.ZonedDateTime

internal class SakerDTOTest {

    private val objectMapper = jsonConfig()

    @Test
    fun `Skal kunne serialiseres til JSON`() {
        val dto = sisteSakstemaForDev()

        val json = objectMapper.encodeToString(dto)

        json.shouldNotBeNull()
    }

}

private fun sisteSakstemaForDev() = SakerDTO(
    listOf(
        SakstemaTestData.temaDagpenger(),
        SakstemaTestData.temaBil()
    ),
    URL("https://person.dev.nav.no/mine-saker"),
    ZonedDateTime.now().minusDays(2)
)

