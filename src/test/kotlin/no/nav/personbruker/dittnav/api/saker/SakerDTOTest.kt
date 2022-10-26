package no.nav.personbruker.dittnav.api.saker

import io.kotest.matchers.nulls.shouldNotBeNull
import kotlinx.serialization.encodeToString
import no.nav.personbruker.dittnav.api.config.jsonConfig
import org.junit.jupiter.api.Test
import java.net.URL
import java.time.ZonedDateTime

internal class SakerDTOTest {

    @Test
    fun `Skal kunne serialiseres til JSON`() {
        val dto = SakerDTO(
            listOf(
                SakstemaDTO(
                    "Dagpenger",
                    "DAG",
                    ZonedDateTime.now().minusDays(9),
                    URL("https://dummy/DAG")
                ),
                SakstemaDTO(
                    "Bil",
                    "BIL",
                    ZonedDateTime.now().minusDays(2),
                    URL("https://dummy/BIL")
                )
            ),
            URL("https://person.dev.nav.no/mine-saker"),
            ZonedDateTime.now().minusDays(2)
        )
        val json = jsonConfig().encodeToString(dto)
        json.shouldNotBeNull()
    }
}

