package no.nav.personbruker.dittnav.api.oppfolging;

import io.kotest.matchers.shouldBe
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import io.ktor.server.testing.testApplication
import no.nav.personbruker.dittnav.api.applicationHttpClient
import no.nav.personbruker.dittnav.api.assert
import no.nav.personbruker.dittnav.api.authenticatedGet
import no.nav.personbruker.dittnav.api.bool
import no.nav.personbruker.dittnav.api.mockApi
import no.nav.personbruker.dittnav.api.externalServiceWithJsonResponse
import no.nav.personbruker.dittnav.api.toJsonObject
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import java.net.URL


class OppfolgingApiTest {
    private val testHost = "https://test.host.no"
    private val apiEndpoint = "/api/niva3/underoppfolging"

    @ParameterizedTest
    @ValueSource(booleans = [true, false])
    fun `Under oppfølging`(forventetOppfølgingStaus: Boolean) = testApplication {
        mockApi(
            oppfolgingConsumer = OppfolgingConsumer(
                client = applicationHttpClient(),
                oppfolgingApiBaseURL = URL(testHost)
            )
        )
        externalServiceWithJsonResponse(
            hostApiBase = testHost,
            apiEndpoint,
            content = externalOppfølgingJson(forventetOppfølgingStaus)
        )
        client.authenticatedGet("dittnav-api/oppfolging").assert {
            status shouldBe HttpStatusCode.OK
            val jsonBody = bodyAsText().toJsonObject().bool("erBrukerUnderOppfolging")
            jsonBody shouldBe forventetOppfølgingStaus
        }
    }

    @Test
    fun `500 når dittnav-api feiler mot eventhandlerApi`() = testApplication {
        mockApi(
            oppfolgingConsumer = OppfolgingConsumer(
                client = applicationHttpClient(),
                oppfolgingApiBaseURL = URL(testHost)
            )
        )

        externalServices {
            hosts(testHost) {
                routing {
                    get(apiEndpoint) {
                        call.respond(HttpStatusCode.InternalServerError)
                    }
                }
            }
        }
        client.authenticatedGet("dittnav-api/oppfolging").status shouldBe HttpStatusCode.InternalServerError
    }
}

private fun externalOppfølgingJson(underOppfølging: Boolean) = """
    {
     "underOppfolging": $underOppfølging
     }
""".trimIndent()
