package no.nav.personbruker.dittnav.api.saker;

import io.kotest.matchers.shouldBe
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.server.testing.testApplication
import io.mockk.coEvery
import io.mockk.mockk
import no.nav.personbruker.dittnav.api.applicationHttpClient
import no.nav.personbruker.dittnav.api.assert
import no.nav.personbruker.dittnav.api.authenticatedGet
import no.nav.personbruker.dittnav.api.mockApi
import no.nav.personbruker.dittnav.api.externalServiceWithJsonResponse
import org.junit.jupiter.api.Test
import java.net.URL
import java.time.ZonedDateTime

class SakerApiTest {
    private val minesakerTestUrl = "https://ut.nav/saker-test"
    private val sakerBaseUrl = "https://inn.saker.test.nav"
    private val saksteamaerEndpoint = "/sakstemaer/sistendret"
    private val mockToken = "mocktoken"
    private val tokendingsMock: MineSakerTokendings = mockk<MineSakerTokendings>().also {
        coEvery { it.exchangeToken(any()) } returns mockToken
    }


    @Test
    fun ` Saker for bruker`() {
        val expectedSakerDTO =
            createSakerDto(
                dagpengerSistEndret = ZonedDateTime.now().minusDays(2),
                sakstemaer = listOf(
                    createSakstemaDTO(),
                    createSakstemaDTO(navn = "annet tema", kode = "erkoderspesifikt"),
                    createSakstemaDTO(navn = "saksnavn", detaljvisningUrl = "https://detaljer.nav.test/saksnavn")
                )
            )

        testApplication {
            mockApi(sakerConsumer = mineSakerConsumer())
            externalServiceWithJsonResponse(sakerBaseUrl, saksteamaerEndpoint, expectedSakerDTO.mapToEksternJson())
            client.authenticatedGet("dittnav-api/saker").assert {
                status shouldBe HttpStatusCode.OK
            }
        }
    }

    private fun ApplicationTestBuilder.mineSakerConsumer() = MineSakerConsumer(
        client =
        applicationHttpClient(),
        mineSakerUrl = URL(minesakerTestUrl),
        mineSakerTokendings = tokendingsMock,
        mineSakerApiURL = URL(sakerBaseUrl)
    )
}
