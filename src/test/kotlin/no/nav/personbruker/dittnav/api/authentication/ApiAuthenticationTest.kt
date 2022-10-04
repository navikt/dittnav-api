package no.nav.personbruker.dittnav.api.authentication

import io.kotest.matchers.shouldBe
import io.ktor.client.request.header
import io.ktor.client.request.request
import io.ktor.client.request.url
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import io.ktor.server.testing.testApplication
import no.nav.personbruker.dittnav.api.config.LoginserviceMetadata
import no.nav.personbruker.dittnav.api.mockApi
import no.nav.personbruker.dittnav.api.respondRawJson
import no.nav.personbruker.dittnav.api.stubToken
import no.nav.personbruker.dittnav.api.util.applicationHttpClient
import org.junit.jupiter.api.Test

class ApiAuthenticationTest {
    private val authCheckEndpoint = "/dittnav-api/authPing"
    private val mockWellknown = this::class.java.classLoader.getResource("wellknown_dummy.json").readText()

    @Test
    fun `200 for autorisert request`() {
        testApplication {
            mockApi()
            client.request {
                url(authCheckEndpoint)
                method = HttpMethod.Get
                header(HttpHeaders.Cookie, "selvbetjening-idtoken=$stubToken")
            }.status shouldBe HttpStatusCode.OK


        }
    }

    @Test
    fun `401 if authcoockie is missing`() {
        testApplication {
            mockApi()
            client.request {
                url(authCheckEndpoint)
                method = HttpMethod.Get
            }.status shouldBe HttpStatusCode.Unauthorized

        }
    }

    @Test
    fun `henter loginservice credentials`() {
        testApplication {
            externalServices {
                hosts("https://dummydiscovery.test"){
                    routing {
                        get("/wellknown"){
                            call.respondRawJson(mockWellknown)
                        }
                    }
                }

            }
            val client = applicationHttpClient()
            val result = LoginserviceMetadata.get(client, "https://dummydiscovery.test/wellknown")
            result.jwks_uri shouldBe "https://dummy.no/dummy-oidc-provider/jwk"
            result.issuer shouldBe "https://dummy.no/dummy-oidc-provider/"
        }
    }
}