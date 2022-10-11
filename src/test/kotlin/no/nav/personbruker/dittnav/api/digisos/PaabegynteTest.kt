package no.nav.personbruker.dittnav.api.digisos

import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import kotlinx.serialization.decodeFromString
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
        val paabegynte = jsonConfig().decodeFromString<Paabegynte>(respons)
        paabegynte.shouldNotBeNull()
    }

    @Test
    fun `Skal kunne konvertere til intern modell`() {
        val external = påbegyntSøknad()

        val internal = external.toInternal()

        internal.eventId shouldBe external.eventId
        internal.forstBehandlet shouldBe external.eventTidspunkt.toPaabegynteZonedDateTime()
        internal.grupperingsId shouldBe external.grupperingsId
        internal.tekst shouldBe external.tekst
        internal.link shouldBe external.link
        internal.aktiv shouldBe external.isAktiv
        internal.sistOppdatert shouldBe external.sistOppdatert.toPaabegynteZonedDateTime()
        internal.produsent shouldBe "digiSos"
    }

    @Test
    fun `Skal konvertere flere eksterne til interne samtidig`() {
        val externals = listOf(
            påbegyntSøknad(true),
            påbegyntSøknad(false)
        )

        val internals = externals.toInternals()

        internals.shouldNotBeNull()
        internals.size shouldBe externals.size
        internals[0].shouldNotBeNull()
        internals[1].shouldNotBeNull()
    }

    @Test
    fun `Skal kappe tekster som er for lange`() {
        val eventMedForLangTekst = påbegyntSøknad().copy(
            tekst = "A".repeat(Paabegynte.maxBeskjedTextLength + 1)
        )

        val internal = eventMedForLangTekst.toInternal()

        internal.tekst.length shouldBe Paabegynte.maxBeskjedTextLength
        internal.tekst.endsWith("...") shouldBe true
    }

}

private fun påbegyntSøknad(active: Boolean = false) = Paabegynte(
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
