package no.nav.personbruker.dittnav.api.unleash;

import io.kotest.matchers.shouldBe
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.testApplication
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.mockk
import no.finn.unleash.Unleash
import no.nav.personbruker.dittnav.api.authenticatedGet
import no.nav.personbruker.dittnav.api.authentication.AuthenticatedUserTestData
import no.nav.personbruker.dittnav.api.mockApi
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource


class UnleashApiTest {

    private val unleashClientMock = mockk<Unleash>()
    private val authenticatedUser = AuthenticatedUserTestData.createAuthenticatedUser("1234567")

    @BeforeEach
    fun clearMock(){
        clearMocks(unleashClientMock)
    }
    @ParameterizedTest
    @CsvSource(
        "minside, minSideEnabled, false",
        "minside, minSideEnabled, true",
        "situasjon, veientilarbeid.kanViseUtfraSituasjon, false",
        "situasjon, veientilarbeid.kanViseUtfraSituasjon, true",
    )
    fun testUnleashApi(endpoint: String, togglename: String, enabled: Boolean) {
        testApplication {
            mockApi(unleashService = UnleashService(unleashClientMock))
            coEvery { unleashClientMock.isEnabled(togglename, any(), false) } returns enabled
            client.authenticatedGet("dittnav-api/unleash/$endpoint", authenticatedUser).apply {
                status shouldBe HttpStatusCode.OK
                bodyAsText() shouldBe "$enabled"
            }
        }
    }
}