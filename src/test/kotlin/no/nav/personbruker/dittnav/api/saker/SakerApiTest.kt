package no.nav.personbruker.dittnav.api.saker;

import io.kotest.matchers.shouldBe
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.testApplication
import io.mockk.coEvery
import io.mockk.mockk
import no.nav.personbruker.dittnav.api.applicationHttpClient
import no.nav.personbruker.dittnav.api.authenticatedGet
import no.nav.personbruker.dittnav.api.mockApi
import no.nav.personbruker.dittnav.api.setupExternalServiceWithJsonResponse
import no.nav.personbruker.dittnav.api.tokenx.AccessToken
import org.intellij.lang.annotations.Language
import org.junit.jupiter.api.Test
import java.net.URL
import java.time.ZonedDateTime
import kotlin.text.StringBuilder

private const val minesakerTestUrl = "https://ut.nav/saker-test"

class SakerApiTest {
    private val sakerBaseUrl = "https://inn.saker.test.nav"
    private val saksteamaerEndpoint = "/sakstemaer/sistendret"
    private val mockToken = "mocktoken"
    private val tokendingsMock: MineSakerTokendings = mockk<MineSakerTokendings>().also {
        coEvery { it.exchangeToken(any()) } returns AccessToken(mockToken)
    }


    @Test
    fun `henter saker for bruker`() {
        val exoectedSakerDTO =
            createSakerDto(
                dagpengerSistEndret = ZonedDateTime.now().minusDays(2),
                sakstemaer = listOf(
                    createSakstemaDTO(),
                    createSakstemaDTO(navn = "annet tema", kode = "erkoderspesifikt"),
                    createSakstemaDTO(navn = "saksnavn", detaljvisningUrl = "https://detaljer.nav.test/saksnavn")
                )
            )

        testApplication {
            val consumer = MineSakerConsumer(client = applicationHttpClient(), mineSakerApiURL = URL(sakerBaseUrl))
            val service = SakerService(
                mineSakerConsumer = consumer,
                mineSakerUrl = URL(minesakerTestUrl),
                mineSakerTokendings = tokendingsMock
            )
            mockApi(sakerService = service)
            setupExternalServiceWithJsonResponse(sakerBaseUrl, saksteamaerEndpoint, exoectedSakerDTO.mapToEksternJson())
            client.authenticatedGet("dittnav-api/saker").apply {
                status shouldBe HttpStatusCode.OK
                //   TODO("Please write your test here")
            }
        }
    }
}

@Language("JSON")
private fun SakerDTO.mapToEksternJson(): String = """
    {
      "dagpengerSistEndret": "${this.dagpengerSistEndret}",
      "sistEndrede": [${this.sakstemaer.mapToEksternJson()}]
    }
""".trimIndent()

fun List<SakstemaDTO>.mapToEksternJson(): String {
    val iterator = this.iterator()
    return StringBuilder().also { strBuilder ->
        while (iterator.hasNext()) {
            strBuilder.append(iterator.next().mapToEksternJson())
            if (iterator.hasNext()) {
                strBuilder.append(",")
            }

        }
    }.toString()
}

private fun SakstemaDTO.mapToEksternJson(): String = """
    {
        "navn":"$navn",
        "kode":"$kode",
        "sistEndret": "$sistEndret",
        "detaljvisningUrl": "$detaljvisningUrl"
    }
    
""".trimIndent()

private fun createSakerDto(
    dagpengerSistEndret: ZonedDateTime? = null,
    sakstemaer: List<SakstemaDTO> = listOf(createSakstemaDTO())
) = SakerDTO(sakstemaer = sakstemaer, sakerURL = URL(minesakerTestUrl), dagpengerSistEndret = dagpengerSistEndret)

private fun createSakstemaDTO(
    navn: String = "navnernavn",
    kode: String = "kodeErKode",
    sistEndret: ZonedDateTime = ZonedDateTime.now(),
    detaljvisningUrl: String = "https://default.test"
) =
    SakstemaDTO(navn = navn, kode = kode, sistEndret = sistEndret, detaljvisningUrl = URL(detaljvisningUrl))
