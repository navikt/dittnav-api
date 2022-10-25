package no.nav.personbruker.dittnav.api.digisos

import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import io.ktor.server.testing.testApplication
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import no.nav.personbruker.dittnav.api.TestUser
import no.nav.personbruker.dittnav.api.applicationHttpClient
import no.nav.personbruker.dittnav.api.beskjed.BeskjedDTO
import no.nav.personbruker.dittnav.api.beskjed.KildeType
import no.nav.personbruker.dittnav.api.externalServiceWithJsonResponse
import no.nav.personbruker.dittnav.api.tokenx.AccessToken
import org.intellij.lang.annotations.Language
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import java.net.URL

internal class DigiSosConsumerTest {

    private val dummyUser = TestUser.createAuthenticatedUser()
    private val mockTokendings = mockk<DigiSosTokendings>().also {
        coEvery { it.exchangeToken(any()) } returns AccessToken("Access!")
    }
    private val digiSosSoknadBaseURL = "https://soknad"

    @Test
    fun `Henter paabegynte aktive soknader`() {
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

            DigiSosConsumer(applicationHttpClient(), mockTokendings, URL(digiSosSoknadBaseURL))
                .getPaabegynteActive(dummyUser)
                .apply {
                    successFullSources().shouldContainExactly(KildeType.DIGISOS)
                    failedSources().size shouldBe 0
                    val results = results()
                    results.shouldNotBeNull()
                    results.size shouldBe 3
                    results.all { it.aktiv } shouldBe true
                    results shouldContainEventId "12345"
                    results shouldContainEventId "8765"
                    results shouldContainEventId "98659"

                }
        }
    }

    @Test
    fun `Henter paabegynte inaktive soknader`() {
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

            DigiSosConsumer(applicationHttpClient(), mockTokendings, URL(digiSosSoknadBaseURL))
                .getPaabegynteInactive(dummyUser)
                .apply {
                    successFullSources().shouldContainExactly(KildeType.DIGISOS)
                    failedSources().size shouldBe 0
                    val results = results()
                    results.size shouldBe 4
                    results.all { !it.aktiv } shouldBe true
                    results shouldContainEventId "12345"
                    results shouldContainEventId "8765"
                    results shouldContainEventId "98659"
                    results shouldContainEventId "98633"
                }
        }
    }

    @Test
    fun `Håndterer feil fra digisosApi`() =
        testApplication {
            val digiSosConsumer = DigiSosConsumer(
                client = applicationHttpClient(),
                tokendings = mockTokendings,
                digiSosSoknadBaseURL = URL(digiSosSoknadBaseURL)
            )
            externalServices {
                hosts(digiSosSoknadBaseURL) {
                    routing {
                        get("/dittnav/pabegynte/aktive") {
                            call.respond(HttpStatusCode.InternalServerError)
                        }
                        get("/dittnav/pabegynte/inaktive") {
                            call.respond(HttpStatusCode.BadRequest)
                        }
                    }
                }
            }

            digiSosConsumer.getPaabegynteActive(dummyUser).failedSources().shouldContainExactly(KildeType.DIGISOS)
            digiSosConsumer.getPaabegynteInactive(dummyUser).failedSources().shouldContainExactly(KildeType.DIGISOS)

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
