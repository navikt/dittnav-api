package no.nav.personbruker.dittnav.api.digisos

import kotlinx.serialization.decodeFromString
import no.nav.personbruker.dittnav.api.config.json
import org.amshove.kluent.shouldNotBeNull
import org.junit.jupiter.api.Test

internal class EttersendelseTest {

    private val respons = """
    {
        "eventId": "e456",
        "eventTidspunkt": "2021-08-11T13:38:22.522",
        "grupperingsId": "gid003",
        "tekst": "Vi mangler vedlegg for å kunne behandle søknaden din om økonomisk sosialhjelp",
        "link": "https://digisos.dev/status",
        "sikkerhetsnivaa": 3,
        "sistOppdatert": "2021-08-11T13:38:22.522",
        "aktiv": true
    }""".trimIndent()

    @Test
    fun `Skal kunne deserialisere en respons fra DigiSos`() {
        val objectMapper = json()

        val ettersendelse = objectMapper.decodeFromString<Ettersendelse>(respons)

        ettersendelse.shouldNotBeNull()
    }

}