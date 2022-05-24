package no.nav.personbruker.dittnav.api.digisos

import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.respondError
import io.ktor.client.features.json.JsonFeature
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import no.nav.personbruker.dittnav.api.beskjed.BeskjedDTO
import no.nav.personbruker.dittnav.api.common.AuthenticatedUserObjectMother
import no.nav.personbruker.dittnav.api.config.json
import org.junit.jupiter.api.Test
import java.net.URL

internal class DigiSosClientTest {

    private val dummyUser = AuthenticatedUserObjectMother.createAuthenticatedUser()

    private val digiSosSoknadBaseURL = URL("https://soknad")

    @Test
    fun `Skal kunne hente paabegynte aktive soknader`() {
        val expectedStatus = true
        val clientMock = createDigiSosClientWithMockedResponses(expectedStatus)
        val digiSosClient = DigiSosClient(clientMock, digiSosSoknadBaseURL)

        val result : List<BeskjedDTO> = runBlocking {
            digiSosClient.getPaabegynteActive(dummyUser)
        }

        result.shouldNotBeNull()
        result[0]::class shouldBe BeskjedDTO::class
        result[0].aktiv shouldBe expectedStatus
    }

    @Test
    fun `Skal kunne hente paabegynte inaktive soknader`() {
        val expectedStatus = false
        val clientMock = createDigiSosClientWithMockedResponses(expectedStatus)
        val digiSosClient = DigiSosClient(clientMock, digiSosSoknadBaseURL)

        val result : List<BeskjedDTO> = runBlocking {
            digiSosClient.getPaabegynteInactive(dummyUser)
        }

        result.shouldNotBeNull()
        result[0]::class shouldBe BeskjedDTO::class
        result[0].aktiv shouldBe expectedStatus
    }

    private fun createDigiSosClientWithMockedResponses(activeEvents: Boolean): HttpClient {
        val clientMock = HttpClient(MockEngine) {
            install(JsonFeature)
            engine {
                addHandler { request ->
                    if (request.url.encodedPath.contains("pabegynte/")) {
                        respond(
                            json().encodeToString(listOf(PaabegynteObjectMother.giveMeOne(activeEvents))),
                            headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                        )

                    } else {
                        respondError(HttpStatusCode.BadRequest, "Noe feilet ved kalle til ${request.url}")
                    }
                }
            }
        }
        return clientMock
    }

}
