package no.nav.personbruker.dittnav.api.digisos

import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import io.ktor.server.testing.testApplication
import kotlinx.coroutines.runBlocking
import no.nav.personbruker.dittnav.api.beskjed.BeskjedDTO
import no.nav.personbruker.dittnav.api.common.AuthenticatedUserObjectMother
import no.nav.personbruker.dittnav.api.config.jsonConfig
import no.nav.personbruker.dittnav.api.respondRawJson
import no.nav.personbruker.dittnav.api.util.applicationHttpClient
import org.intellij.lang.annotations.Language
import org.junit.jupiter.api.Test
import java.net.URL

internal class DigiSosClientTest {

    private val dummyUser = AuthenticatedUserObjectMother.createAuthenticatedUser()

    private val digiSosSoknadBaseURL = "https://soknad"

    @Test
    fun `Skal kunne hente paabegynte aktive soknader`() {
        val expectedStatus = true

        testApplication {
            externalServices {
                hosts(digiSosSoknadBaseURL) {
                    install(ContentNegotiation) {
                        jsonConfig()
                    }
                    routing {
                        get("dittnav/pabegynte/aktive") {
                            call.respondRawJson(
                                """[${rawDigiSosResponse("12345", expectedStatus)},
                                    ${rawDigiSosResponse(eventId = "8765", expectedStatus)},
                                    ${rawDigiSosResponse(eventId = "98659", expectedStatus)}]""".trimMargin()
                            )
                        }

                    }
                }
            }
            val digiSosClient = DigiSosClient(applicationHttpClient(), URL(digiSosSoknadBaseURL))

            val result: List<BeskjedDTO> = runBlocking {
                digiSosClient.getPaabegynteActive(dummyUser)
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

        testApplication {
            externalServices {
                hosts(digiSosSoknadBaseURL) {
                    install(ContentNegotiation) {
                        jsonConfig()
                    }
                    routing {
                        get("dittnav/pabegynte/inaktive") {
                            call.respondRawJson(
                                """[${rawDigiSosResponse(eventId = "12345", expectedStatus)},
                                    ${rawDigiSosResponse(eventId = "8765", expectedStatus)},
                                    ${rawDigiSosResponse(eventId = "98659", expectedStatus)}, 
                                    ${rawDigiSosResponse(eventId = "98633", expectedStatus)}]""".trimMargin()
                            )
                        }

                    }
                }
            }
            val digiSosClient = DigiSosClient(applicationHttpClient(), URL(digiSosSoknadBaseURL))

            val result: List<BeskjedDTO> = runBlocking {
                digiSosClient.getPaabegynteInactive(dummyUser)
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