package no.nav.personbruker.dittnav.api.digisos

import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.respondError

import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.server.testing.TestApplicationBuilder
import io.ktor.server.testing.testApplication
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import no.nav.personbruker.dittnav.api.beskjed.BeskjedDTO
import no.nav.personbruker.dittnav.api.common.AuthenticatedUserObjectMother
import no.nav.personbruker.dittnav.api.config.jsonConfig
import no.nav.personbruker.dittnav.api.util.applicationHttpClient
import org.junit.jupiter.api.Test
import java.net.URL
import java.time.LocalDateTime

internal class DigiSosClientTest {

    private val dummyUser = AuthenticatedUserObjectMother.createAuthenticatedUser()

    private val digiSosSoknadBaseURL = URL("https://soknad")

    @Test
    fun `Skal kunne hente paabegynte aktive soknader`() {
        val expectedStatus = true

        testApplication {
            mockDigiSosService(expectedStatus)
            val digiSosClient = DigiSosClient(applicationHttpClient(), digiSosSoknadBaseURL)

            val result: List<BeskjedDTO> = runBlocking {
                digiSosClient.getPaabegynteInactive(dummyUser)
            }

            result.shouldNotBeNull()
            result[0]::class shouldBe BeskjedDTO::class
            result[0].aktiv shouldBe expectedStatus
        }
    }

    @Test
    fun `Skal kunne hente paabegynte inaktive soknader`() {
        val expectedStatus = false

        testApplication {
            mockDigiSosService(expectedStatus)
            val digiSosClient = DigiSosClient(applicationHttpClient(), digiSosSoknadBaseURL)

            val result: List<BeskjedDTO> = runBlocking {
                digiSosClient.getPaabegynteInactive(dummyUser)
            }

            result.shouldNotBeNull()
            result[0]::class shouldBe BeskjedDTO::class
            result[0].aktiv shouldBe expectedStatus
        }
    }

}

private fun TestApplicationBuilder.mockDigiSosService(activeEvents: Boolean) {
    externalServices {
        /* TODO
              addHandler { request ->
                  if (request.url.encodedPath.contains("pabegynte/")) {
                      respond(
                          jsonConfig().encodeToString(listOf(påbegyntSøknad(activeEvents))),
                          headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                      )

                  } else {
                      respondError(HttpStatusCode.BadRequest, "Noe feilet ved kalle til ${request.url}")
                  }
              }
          }
      }*/
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

