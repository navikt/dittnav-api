package no.nav.personbruker.dittnav.api.saker

import io.kotest.matchers.shouldBe
import kotlinx.serialization.decodeFromString
import no.nav.personbruker.dittnav.api.assert
import no.nav.personbruker.dittnav.api.config.jsonConfig
import org.junit.jupiter.api.Test
import java.net.URL
import java.time.ZonedDateTime

internal class SakstemaTest {
    private val sakstemaDagpenger = Sakstema(
        "Dagpenger",
        "DAG",
        ZonedDateTime.now().minusDays(8),
        URL("https://dummy/DAG")
    )

    private val sakstemaSosialhjelp = Sakstema(
        "Økonomisk sosialhjelp",
        "KOM",
        ZonedDateTime.now().minusDays(24),
        URL("https://dummy/KOM")
    )


    @Test
    fun `Deserialiserer respons fra mine saker`() {
        val expectedDetaljvisningsurl = "https://person.dev.nav.no/mine-saker/tema/GEN"
        """
        {
          "navn": "Generell",
          "kode": "GEN",
          "sistEndret": "2021-09-15T10:57:56Z",
          "detaljvisningUrl": "$expectedDetaljvisningsurl"
        }"""
            .let { jsonConfig().decodeFromString<Sakstema>(it) }
            .assert {
                kode shouldBe "GEN"
                navn shouldBe "Generell"
                detaljvisningUrl shouldBe URL(expectedDetaljvisningsurl)
            }
    }

    @Test
    fun `Konverterer fra eksterne Sakstema til intern modell`() {
        val external = sakstemaDagpenger
        external.toInternal().assert {
            kode shouldBe external.kode
            navn shouldBe external.navn
            sistEndret shouldBe external.sistEndret
            detaljvisningUrl shouldBe external.detaljvisningUrl
        }


    }

    @Test
    fun `Konverterer eksterne SisteSakstemaer til intern modell`() {
        val external = SisteSakstemaer(
            listOf(sakstemaDagpenger, sakstemaSosialhjelp),
            ZonedDateTime.now()
        )
        external.toInternal().assert {
            sakstemaer.size shouldBe external.sistEndrede.size
            dagpengerSistEndret shouldBe external.dagpengerSistEndret
        }

    }

}

