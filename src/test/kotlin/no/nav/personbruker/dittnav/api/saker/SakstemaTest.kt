package no.nav.personbruker.dittnav.api.saker

import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import kotlinx.serialization.decodeFromString
import no.nav.personbruker.dittnav.api.config.jsonConfig
import org.junit.jupiter.api.Test
import java.net.URL
import java.time.ZonedDateTime

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

    @Test
    fun `Skal kunne konvertere fra eksterne Sakstema til intern modell`() {
        val external = sakstemaDagpenger()

        val internal = external.toInternal()

        internal.kode shouldBe external.kode
        internal.navn shouldBe external.navn
        internal.sistEndret shouldBe external.sistEndret
        internal.detaljvisningUrl shouldBe external.detaljvisningUrl
    }

    @Test
    fun `Skal kunne konvertere eksterne SisteSakstemaer til intern modell`() {
        val external = SisteSakstemaer(
            listOf(sakstemaDagpenger(), sakstemaSosialhjelp()),
            ZonedDateTime.now()
        )

        val internal = external.toInternal()

        internal.sakstemaer.size shouldBe external.sistEndrede.size
        internal.dagpengerSistEndret shouldBe external.dagpengerSistEndret
    }

}

private fun sakstemaDagpenger() = Sakstema(
    "Dagpenger",
    "DAG",
    ZonedDateTime.now().minusDays(8),
    URL("https://dummy/DAG")
)

private fun sakstemaSosialhjelp() = Sakstema(
    "Økonomisk sosialhjelp",
    "KOM",
    ZonedDateTime.now().minusDays(24),
    URL("https://dummy/KOM")
)
