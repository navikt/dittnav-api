package no.nav.personbruker.dittnav.api.saker

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.server.testing.testApplication
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import no.nav.personbruker.dittnav.api.TestUser
import no.nav.personbruker.dittnav.api.applicationHttpClient
import no.nav.personbruker.dittnav.api.config.ConsumeSakerException
import no.nav.personbruker.dittnav.api.externalServiceWith500Response
import no.nav.personbruker.dittnav.api.externalServiceWithJsonResponse
import no.nav.personbruker.dittnav.api.mockApi
import no.nav.personbruker.dittnav.api.tokenx.AccessToken
import org.junit.jupiter.api.Test
import java.net.URL

internal class MineSakerConsumerTest {

    private val mineSakerURL = "https://ut.nav/saker-test"
    private val sakerBaseUrl = "https://inn.saker.test.nav"
    private val dummyUser = TestUser.createAuthenticatedUser()
    private val saksteamaerEndpoint = "/sakstemaer/sistendret"
    private val tokendingsMock = mockk<MineSakerTokendings>().also {
        coEvery { it.exchangeToken(any()) } returns AccessToken("123")
    }


    @Test
    fun `Skal hente sakstemaer fra Mine Saker`() =
        testApplication {
            val expectedSakerDTO = createSakerDto(minesakerTestUrl = mineSakerURL)
            externalServiceWithJsonResponse(sakerBaseUrl, saksteamaerEndpoint, expectedSakerDTO.mapToEksternJson())
            mineSakerConsumer().hentSistEndredeSakstemaer(dummyUser) shouldBe expectedSakerDTO
        }

    @Test
    fun `Skal fange og kaste feil videre hvis noe feiler`() = testApplication {
        externalServiceWith500Response(sakerBaseUrl,saksteamaerEndpoint)
        shouldThrow<ConsumeSakerException> { mineSakerConsumer().hentSistEndredeSakstemaer(dummyUser) }
    }


    private fun ApplicationTestBuilder.mineSakerConsumer() = MineSakerConsumer(
        client =
        applicationHttpClient(),
        mineSakerUrl = URL(mineSakerURL),
        mineSakerTokendings = tokendingsMock,
        mineSakerApiURL = URL(sakerBaseUrl)
    )

}