package no.nav.personbruker.dittnav.api.meldekort;

import io.kotest.matchers.shouldBe
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.server.testing.testApplication
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import no.nav.personbruker.dittnav.api.applicationHttpClient
import no.nav.personbruker.dittnav.api.authenticatedGet
import no.nav.personbruker.dittnav.api.bool
import no.nav.personbruker.dittnav.api.int
import no.nav.personbruker.dittnav.api.isNullObject
import no.nav.personbruker.dittnav.api.localdate
import no.nav.personbruker.dittnav.api.localdateOrNull
import no.nav.personbruker.dittnav.api.mockApi
import no.nav.personbruker.dittnav.api.externalServiceWithJsonResponse
import no.nav.personbruker.dittnav.api.string
import no.nav.personbruker.dittnav.api.toJsonObject
import no.nav.personbruker.dittnav.api.tokenx.AccessToken
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.lang.AssertionError
import java.net.URL
import java.time.LocalDate


class MeldekortApiTest {
    private val meldekortTokendings: MeldekortTokendings = mockk()
    private val meldekortApiBase = "https://nav.meldkort.test"
    private val medlekortStatusEndpoint = "/api/person/meldekortstatus"

    @BeforeEach()
    fun `mock tokendings`() {
        coEvery { meldekortTokendings.exchangeToken(any()) } returns AccessToken("1236")
    }

    @Test
    fun `henter meldekortinfo for autentisert bruker`() {
        val expectedMeldekortInfo = createMeldekortinfo(
            nyeMeldekort = createNyeMeldekortObject(
                antall = 3,
                nesteMeldekort = createInternalMedldekortObject(),
                nesteInnsendingAvMeldekort = LocalDate.now().plusDays(5)

            )
        )

        testApplication {
            mockApi(meldekortService = MeldekortService(meldekortConsumer(), meldekortTokendings))

            externalServiceWithJsonResponse(
                hostApiBase = meldekortApiBase,
                endpoint = medlekortStatusEndpoint,
                content = externalStatusJson(expectedMeldekortInfo)
            )

            client.authenticatedGet("dittnav-api/meldekortinfo").apply {
                status shouldBe HttpStatusCode.OK
                val jsonResponse = bodyAsText().toJsonObject()
                jsonResponse.int("resterendeFeriedager") shouldBe expectedMeldekortInfo.resterendeFeriedager
                jsonResponse.int("etterregistrerteMeldekort") shouldBe expectedMeldekortInfo.etterregistrerteMeldekort
                jsonResponse.bool("meldekortbruker") shouldBe true
                jsonResponse["nyeMeldekort"]?.jsonObject?.also { nyeMeldekortJson ->
                    nyeMeldekortJson.int("antallNyeMeldekort") shouldBe expectedMeldekortInfo.nyeMeldekort.antallNyeMeldekort
                    nyeMeldekortJson.localdateOrNull("nesteInnsendingAvMeldekort") shouldBe expectedMeldekortInfo.nyeMeldekort.nesteInnsendingAvMeldekort
                    nyeMeldekortJson.meldekort("nesteMeldekort")?.also { resultMeldekort ->
                        val expectedMeldekort = expectedMeldekortInfo.nyeMeldekort.nesteMeldekort!!
                        resultMeldekort.fra shouldBe expectedMeldekort.fra
                        resultMeldekort.til shouldBe expectedMeldekort.til
                        resultMeldekort.uke shouldBe expectedMeldekort.uke
                        resultMeldekort.sisteDatoForTrekk shouldBe expectedMeldekort.sisteDatoForTrekk
                        resultMeldekort.risikerTrekk shouldBe expectedMeldekort.risikerTrekk
                        resultMeldekort.kanSendesFra shouldBe expectedMeldekort.kanSendesFra

                    }

                } ?: throw AssertionError("Fant ikke meldekort ikke object med nøkkel nye meldekort i jsonrespons")
            }
        }
    }

    @Test
    fun `henter meldekortinfo for bruker som ikke har meldekort`() = testApplication {
        mockApi(meldekortService = MeldekortService(meldekortConsumer(), meldekortTokendings))

        externalServiceWithJsonResponse(
            hostApiBase = meldekortApiBase,
            endpoint = medlekortStatusEndpoint,
            content = emptyExternalStatusJson
        )

        client.authenticatedGet("dittnav-api/meldekortinfo").apply {
            status shouldBe HttpStatusCode.OK
            val jsonResponse = bodyAsText().toJsonObject()
            jsonResponse.int("resterendeFeriedager") shouldBe 0
            jsonResponse.int("etterregistrerteMeldekort") shouldBe 0
            jsonResponse.bool("meldekortbruker") shouldBe false
            jsonResponse["nyeMeldekort"]?.jsonObject?.apply {
                this.int("antallNyeMeldekort") shouldBe 0
                this.localdateOrNull("nesteInnsendingAvMeldekort") shouldBe null
                this.meldekort("nesteMeldekort") shouldBe null

            } ?: throw AssertionError("Fant ikke meldekort object med nøkkel nyeMeldekort i jsonrespons")
        }
    }

    @Test
    fun `Henter meldekortstatus for autentisert bruker`() {
        val now = LocalDate.now()
        val expectedStatus = MeldekortstatusExternal(
            meldekort = 1,
            etterregistrerteMeldekort = 4,
            antallGjenstaaendeFeriedager = 20,
            nesteMeldekort = MeldekortExternal(
                uke = now.weekNumber(),
                kanSendesFra = now.minusDays(2),
                fra = now.minusDays(14),
                til = now
            ),
            nesteInnsendingAvMeldekort = LocalDate.now().plusDays(14)
        )

        testApplication {
            mockApi(meldekortService = MeldekortService(meldekortConsumer(), meldekortTokendings))
            externalServiceWithJsonResponse(
                hostApiBase = meldekortApiBase,
                endpoint = medlekortStatusEndpoint,
                content = meldekortStatusJson(expectedStatus)
            )

            client.authenticatedGet("dittnav-api/meldekortstatus").apply {
                status shouldBe HttpStatusCode.OK
                val jsonResponse = bodyAsText().toJsonObject()
                jsonResponse.int("meldekort") shouldBe expectedStatus.meldekort
                jsonResponse.int("etterregistrerteMeldekort") shouldBe expectedStatus.etterregistrerteMeldekort
                jsonResponse.int("antallGjenstaaendeFeriedager") shouldBe expectedStatus.antallGjenstaaendeFeriedager
                jsonResponse.localdateOrNull("nesteInnsendingAvMeldekort") shouldBe expectedStatus.nesteInnsendingAvMeldekort
                jsonResponse.externalMeldekort("nesteMeldekort")?.also { meldekortExternal ->
                    meldekortExternal.fra shouldBe expectedStatus.nesteMeldekort!!.fra
                    meldekortExternal.til shouldBe expectedStatus.nesteMeldekort!!.til
                    meldekortExternal.kanSendesFra shouldBe expectedStatus.nesteMeldekort!!.kanSendesFra
                }
            }
        }
    }

    private fun ApplicationTestBuilder.meldekortConsumer(): MeldekortConsumer =
        MeldekortConsumer(client = applicationHttpClient(), meldekortApiBaseURL = URL(meldekortApiBase))

}


private fun externalStatusJson(info: Meldekortinfo) = """
    {
        "meldekort":  ${info.nyeMeldekort.antallNyeMeldekort},
        "etterregistrerteMeldekort": ${info.etterregistrerteMeldekort},
        "antallGjenstaaendeFeriedager": ${info.resterendeFeriedager},
        "nesteInnsendingAvMeldekort": "${info.nyeMeldekort.nesteInnsendingAvMeldekort}",
        "nesteMeldekort" : {
            "uke":"${info.nyeMeldekort.nesteMeldekort?.uke}",
            "kanSendesFra":"${info.nyeMeldekort.nesteMeldekort?.kanSendesFra}",
            "fra":"${info.nyeMeldekort.nesteMeldekort?.fra}",
            "til": "${info.nyeMeldekort.nesteMeldekort?.til}" 
        }
    }
""".trimIndent()

private val emptyExternalStatusJson = """
    {
        "meldekort": 0,
        "etterregistrerteMeldekort":0,
        "antallGjenstaaendeFeriedager": 0
    }
""".trimIndent()

private fun meldekortStatusJson(expectedMeldekort: MeldekortstatusExternal): String {
    val nextMeldekortJson: String = expectedMeldekort.nesteMeldekort?.let {
        """,
          "nesteMeldekort": {
            "uke": "${it.uke}",
            "kanSendesFra": "${it.kanSendesFra}",
            "til": "${it.til}",
            "fra": "${it.fra}"
          }
        """.trimIndent()
    } ?: ""

    //language=JSON
    return """
    {
      "meldekort": ${expectedMeldekort.meldekort},
      "etterregistrerteMeldekort": ${expectedMeldekort.etterregistrerteMeldekort},
      "antallGjenstaaendeFeriedager": ${expectedMeldekort.antallGjenstaaendeFeriedager},
      "nesteInnsendingAvMeldekort": "${expectedMeldekort.nesteInnsendingAvMeldekort}"
      $nextMeldekortJson
    }
  
""".trimIndent()
}


private fun JsonElement?.meldekort(key: String): Meldekort? =
    when {
        this.isNullObject(key) -> null
        this?.jsonObject?.get(key)?.jsonObject.isNullObject(key) -> null
        else -> {
            val meldekortJson = this!!.jsonObject[key]?.jsonObject!!
            Meldekort(
                uke = meldekortJson.string("uke"),
                kanSendesFra = meldekortJson.localdate("kanSendesFra"),
                fra = meldekortJson.localdate("fra"),
                til = meldekortJson.localdate("til"),
                sisteDatoForTrekk = meldekortJson.localdate("sisteDatoForTrekk"),
                risikerTrekk = meldekortJson.bool("risikerTrekk")
            )
        }
    }

private fun JsonObject.externalMeldekort(key: String) =
    if (this.isNullObject(key)) {
        null
    } else {
        this.jsonObject[key]?.jsonObject?.let { meldekortJson ->
            MeldekortExternal(
                uke = meldekortJson.string("uke"),
                kanSendesFra = meldekortJson.localdate("kanSendesFra"),
                fra = meldekortJson.localdate("fra"),
                til = meldekortJson.localdate("til")
            )
        }

    }



