package no.nav.personbruker.dittnav.api.digisos

import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.features.json.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import no.nav.personbruker.dittnav.api.beskjed.BeskjedDTO
import no.nav.personbruker.dittnav.api.common.AuthenticatedUserObjectMother
import no.nav.personbruker.dittnav.api.config.json
import no.nav.personbruker.dittnav.api.oppgave.OppgaveDTO
import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.`should be instance of`
import org.amshove.kluent.`should not be null`
import org.junit.jupiter.api.Test
import java.net.URL

internal class DigiSosClientTest {

    private val dummyUser = AuthenticatedUserObjectMother.createAuthenticatedUser()

    private val digiSosSoknadBaseURL = URL("https://soknad")
    private val digiSosInnsynBaseURL = URL("https://innsyn")

    @Test
    fun `Skal kunne hente paabegynte aktive soknader`() {
        val expectedStatus = true
        val clientMock = createDigiSosClientWithMockedResponses(expectedStatus)
        val digiSosClient = DigiSosClient(clientMock, digiSosSoknadBaseURL, digiSosInnsynBaseURL)

        val result : List<BeskjedDTO> = runBlocking {
            digiSosClient.getPaabegynteActive(dummyUser)
        }

        result.`should not be null`()
        result[0] `should be instance of` BeskjedDTO::class
        result[0].aktiv `should be equal to` expectedStatus
    }

    @Test
    fun `Skal kunne hente paabegynte inaktive soknader`() {
        val expectedStatus = false
        val clientMock = createDigiSosClientWithMockedResponses(expectedStatus)
        val digiSosClient = DigiSosClient(clientMock, digiSosSoknadBaseURL, digiSosInnsynBaseURL)

        val result : List<BeskjedDTO> = runBlocking {
            digiSosClient.getPaabegynteInactive(dummyUser)
        }

        result.`should not be null`()
        result[0] `should be instance of` BeskjedDTO::class
        result[0].aktiv `should be equal to` expectedStatus
    }

    @Test
    fun `Skal kunne hente aktive ettersendelser`() {
        val expectedStatus = true
        val clientMock = createDigiSosClientWithMockedResponses(expectedStatus)
        val digiSosClient = DigiSosClient(clientMock, digiSosSoknadBaseURL, digiSosInnsynBaseURL)

        val result : List<OppgaveDTO> = runBlocking {
            digiSosClient.getEttersendelserActive(dummyUser)
        }

        result.`should not be null`()
        result[0] `should be instance of` OppgaveDTO::class
        result[0].aktiv `should be equal to` expectedStatus
    }

    @Test
    fun `Skal kunne hente inaktive ettersendelser`() {
        val expectedStatus = false
        val clientMock = createDigiSosClientWithMockedResponses(expectedStatus)
        val digiSosClient = DigiSosClient(clientMock, digiSosSoknadBaseURL, digiSosInnsynBaseURL)

        val result : List<OppgaveDTO> = runBlocking {
            digiSosClient.getEttersendelserInactive(dummyUser)
        }

        result.`should not be null`()
        result[0] `should be instance of` OppgaveDTO::class
        result[0].aktiv `should be equal to` expectedStatus
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

                    } else if (request.url.encodedPath.contains("oppgaver/")) {
                        respond(
                            json().encodeToString(listOf(EttersendelseObjectMother.giveMeOne(activeEvents))),
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
