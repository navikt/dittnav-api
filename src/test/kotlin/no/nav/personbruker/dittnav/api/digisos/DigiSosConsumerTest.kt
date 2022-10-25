package no.nav.personbruker.dittnav.api.digisos

import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.ktor.server.testing.testApplication
import kotlinx.coroutines.runBlocking
import no.nav.personbruker.dittnav.api.applicationHttpClient
import no.nav.personbruker.dittnav.api.beskjed.BeskjedDTO
import no.nav.personbruker.dittnav.api.externalServiceWithJsonResponse
import no.nav.personbruker.dittnav.api.tokenx.AccessToken
import org.intellij.lang.annotations.Language
import org.junit.jupiter.api.Test
import java.net.URL

internal class DigiSosConsumerTest {

    private val token = AccessToken("Access!")
    private val digiSosSoknadBaseURL = "https://soknad"

    @Test
    fun `Skal kunne hente paabegynte aktive soknader`() {
        val expectedStatus = true
        @Language("JSON") val digiSosResponse =
            """[
                  ${rawDigiSosResponse("12345", expectedStatus)},
                  ${rawDigiSosResponse(eventId = "8765", expectedStatus)},
                  ${rawDigiSosResponse(eventId = "98659", expectedStatus)}
            ]""".trimMargin()

        testApplication {
            externalServiceWithJsonResponse(
                hostApiBase = digiSosSoknadBaseURL,
                endpoint = "/dittnav/pabegynte/aktive",
                content = digiSosResponse
            )

            val result: List<BeskjedDTO> = runBlocking {
                DigiSosConsumer(applicationHttpClient(), URL(digiSosSoknadBaseURL)).getPaabegynteActive(token)
            }
            result.shouldNotBeNull()
            result.size shouldBe 3
            result.all { it.aktiv } shouldBe true
            result shouldContainEventId "12345"
            result shouldContainEventId "8765"
            result shouldContainEventId "98659"
        }
    }

    @Test
    fun `Skal kunne hente paabegynte inaktive soknader`() {
        val expectedStatus = false
        val digiSosResponse = """[${rawDigiSosResponse(eventId = "12345", expectedStatus)},
                                    ${rawDigiSosResponse(eventId = "8765", expectedStatus)},
                                    ${rawDigiSosResponse(eventId = "98659", expectedStatus)}, 
                                    ${rawDigiSosResponse(eventId = "98633", expectedStatus)}]""".trimMargin()

        testApplication {

            externalServiceWithJsonResponse(
                hostApiBase = digiSosSoknadBaseURL,
                endpoint = "/dittnav/pabegynte/inaktive",
                content = digiSosResponse
            )

            val result: List<BeskjedDTO> = runBlocking {
                DigiSosConsumer(applicationHttpClient(), URL(digiSosSoknadBaseURL)).getPaabegynteInactive(token)
            }

            result.size shouldBe 4
            result.all { !it.aktiv } shouldBe true
            result shouldContainEventId  "12345"
            result shouldContainEventId  "8765"
            result shouldContainEventId  "98659"
            result shouldContainEventId  "98633"
        }
    }

}

private infix fun List<BeskjedDTO>.shouldContainEventId(eventId: String) {
    find { it.eventId == eventId } ?: throw AssertionError("Fant ikke søknad med eventId $eventId i liste")
}

@Language("JSON")
private fun rawDigiSosResponse(eventId: String, active: Boolean) =
    """
    {
        "eventId": "$eventId",
        "eventTidspunkt": "2022-10-04T12:38:53.80262",
        "grupperingsId": "8877",
        "isAktiv": $active,
        "link": "https://nav.no/lenke",
        "sikkerhetsnivaa": 4,
        "sistOppdatert": "2022-10-04T12:38:53.802654",
        "tekst": "Dette er en dummytekst"
    }
""".trimIndent()
