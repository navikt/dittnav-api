package no.nav.personbruker.dittnav.api.authentication

import io.kotest.matchers.shouldBe
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.testApplication
import no.nav.personbruker.dittnav.api.applicationHttpClient
import no.nav.personbruker.dittnav.api.authenticatedGet
import no.nav.personbruker.dittnav.api.config.LoginserviceMetadata
import no.nav.personbruker.dittnav.api.mockApi
import no.nav.personbruker.dittnav.api.externalServiceWithJsonResponse
import org.junit.jupiter.api.Test

class ApiAuthenticationTest {
    private val authCheckEndpoint = "/dittnav-api/authPing"
    private val mockWellknown = this::class.java.classLoader.getResource("wellknown_dummy.json").readText()

    @Test
    fun `200 for autorisert request`() =
        testApplication {
            mockApi()
            client.authenticatedGet(authCheckEndpoint).status shouldBe HttpStatusCode.OK
        }


    @Test
    fun `401 if authcoockie is missing`() =
        testApplication {
            mockApi()
            client.get(authCheckEndpoint).status shouldBe HttpStatusCode.Unauthorized

        }


    @Test
    fun `henter loginservice credentials`() =
        testApplication {
            externalServiceWithJsonResponse(
                hostApiBase = "https://dummydiscovery.test",
                endpoint = "/wellknown",
                content = mockWellknown
            )

            LoginserviceMetadata.get(applicationHttpClient(), "https://dummydiscovery.test/wellknown").apply {
                jwks_uri shouldBe "https://dummy.no/dummy-oidc-provider/jwk"
                issuer shouldBe "https://dummy.no/dummy-oidc-provider/"
            }
        }
}