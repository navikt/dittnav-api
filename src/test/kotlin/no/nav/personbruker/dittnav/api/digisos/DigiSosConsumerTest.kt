package no.nav.personbruker.dittnav.api.digisos

import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.ktor.server.testing.testApplication
import kotlinx.coroutines.runBlocking
import no.nav.personbruker.dittnav.api.applicationHttpClient
import no.nav.personbruker.dittnav.api.beskjed.BeskjedDTO
import no.nav.personbruker.dittnav.api.TestData
import no.nav.personbruker.dittnav.api.externalServiceWithJsonResponse
import no.nav.personbruker.dittnav.api.tokenx.AccessToken
import org.intellij.lang.annotations.Language
import org.junit.jupiter.api.Test
import java.net.URL

internal class DigiSosConsumerTest {

    private val token = AccessToken("Access!")
    private val dummyUser = TestData.createAuthenticatedUser()

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
            result.find { it.eventId == "12345" } ?: throw AssertionError("Fant ikke aktiv søknad med eventId 12345")
            result.find { it.eventId == "8765" } ?: throw AssertionError("Fant ikke aktiv søknad med eventId 8765")
            result.find { it.eventId == "98659" } ?: throw AssertionError("Fant ikke aktiv søknad med eventId 12345")
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
            result.find { it.eventId == "12345" } ?: throw AssertionError("Fant ikke inaktiv søknad med eventId 12345")
            result.find { it.eventId == "8765" } ?: throw AssertionError("Fant ikke inaktiv søknad med eventId 8765")
            result.find { it.eventId == "98659" } ?: throw AssertionError("Fant ikke inaktiv søknad med eventId 12345")
            result.find { it.eventId == "98633" } ?: throw AssertionError("Fant ikke inaktiv søknad med eventId 98633")
        }
    }

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
