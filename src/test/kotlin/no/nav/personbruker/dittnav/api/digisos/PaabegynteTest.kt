package no.nav.personbruker.dittnav.api.digisos

import io.kotest.matchers.nulls.shouldNotBeNull
import kotlinx.serialization.decodeFromString
import no.nav.personbruker.dittnav.api.config.json
import org.junit.jupiter.api.Test

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
        val objectMapper = json()

        val paabegynte = objectMapper.decodeFromString<Paabegynte>(respons)

        paabegynte.shouldNotBeNull()
    }

}
