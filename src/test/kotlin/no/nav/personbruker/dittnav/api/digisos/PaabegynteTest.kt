package no.nav.personbruker.dittnav.api.digisos

import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import kotlinx.serialization.decodeFromString
import no.nav.personbruker.dittnav.api.assert
import no.nav.personbruker.dittnav.api.config.jsonConfig
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime

internal class PaabegynteTest {

    private val respons = """
    {
        "eventTidspunkt": "2021-06-17T09:32:35.416897",
        "eventId": "123456",
        "grupperingsId": "enId",
        "tekst": "Søknad om økonomisk sosialhjelp",
        "link": "https://dummy/enId",
        "sikkerhetsnivaa": 3,
        "sistOppdatert": "2021-07-01T09:32:35.416856",
        "isAktiv": false
    }""".trimIndent()

    @Test
    fun `Skal kunne deserialisere en respons fra DigiSos`() {
        jsonConfig().decodeFromString<Paabegynte>(respons).assert {
            shouldNotBeNull()
        }
    }

    @Test
    fun `Skal kunne konvertere til intern modell`() {
        val external = createPåbegyntSøknad()
        external.toInternal().assert {
            eventId shouldBe external.eventId
            forstBehandlet shouldBe external.eventTidspunkt.toPaabegynteZonedDateTime()
            grupperingsId shouldBe external.grupperingsId
            tekst shouldBe external.tekst
            link shouldBe external.link
            aktiv shouldBe external.isAktiv
            sistOppdatert shouldBe external.sistOppdatert.toPaabegynteZonedDateTime()
            produsent shouldBe "digiSos"
        }
    }

    @Test
    fun `Skal konvertere flere eksterne til interne samtidig`() {
        val externals = listOf(
            createPåbegyntSøknad(true),
            createPåbegyntSøknad(false)
        )

        externals.toInternals().assert {
            shouldNotBeNull()
            size shouldBe externals.size
            this[0].shouldNotBeNull()
            this[1].shouldNotBeNull()
        }


    }

    @Test
    fun `Skal kappe tekster som er for lange`() {
        val eventMedForLangTekst = createPåbegyntSøknad().copy(
            tekst = "A".repeat(Paabegynte.maxBeskjedTextLength + 1)
        )

        val internal = eventMedForLangTekst.toInternal()

        internal.tekst.length shouldBe Paabegynte.maxBeskjedTextLength
        internal.tekst.endsWith("...") shouldBe true
    }

}

private fun createPåbegyntSøknad(active: Boolean = false) = Paabegynte(
    LocalDateTime.now(),
    "123",
    "987",
    "Dette er en dummytekst",
    "https://nav.no/lenke",
    4,
    LocalDateTime.now(),
    active
)

private fun LocalDateTime.toPaabegynteZonedDateTime() = ZonedDateTime.of(this, ZoneId.of("Europe/Oslo"))
