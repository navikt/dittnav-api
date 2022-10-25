package no.nav.personbruker.dittnav.api.personalia;

import io.kotest.matchers.shouldBe
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.server.testing.testApplication
import io.mockk.coEvery
import io.mockk.mockk
import no.nav.personbruker.dittnav.api.applicationHttpClient
import no.nav.personbruker.dittnav.api.authenticatedGet
import no.nav.personbruker.dittnav.api.toJsonObject
import no.nav.personbruker.dittnav.api.mockApi
import no.nav.personbruker.dittnav.api.externalServiceWithJsonResponse
import no.nav.personbruker.dittnav.api.string
import no.nav.personbruker.dittnav.api.tokenx.AccessToken
import org.junit.jupiter.api.Test
import java.net.URL

class PersonaliaApiTest {
    private val testhostBaseApi = "https://personalia.test"
    private val mockTokending = mockk<PersonaliaTokendings>().also {
        coEvery { it.exchangeToken(any()) } returns AccessToken("tadda!")
    }

    @Test
    fun `personalia med ident`() = testApplication {
        mockApi(personaliaConsumer = createPersonaliaConsumer())
        val expectedIdent = "846541550056"
        externalServiceWithJsonResponse(
            hostApiBase = testhostBaseApi,
            endpoint = "/ident",
            content = """ { "ident": "$expectedIdent" } """.trimIndent()

        )

        client.authenticatedGet("dittnav-api/ident").apply {
            status shouldBe HttpStatusCode.OK
            bodyAsText().toJsonObject().string("ident") shouldBe expectedIdent
        }
    }

    @Test
    fun `personalia med navn`() = testApplication {
        mockApi(personaliaConsumer = createPersonaliaConsumer())
        val expectedIdent = "846541550056"
        externalServiceWithJsonResponse(
            hostApiBase = testhostBaseApi,
            endpoint = "/ident",
            content = """ { "ident": "$expectedIdent" } """.trimIndent()

        )

        client.authenticatedGet("dittnav-api/ident").apply {
            status shouldBe HttpStatusCode.OK
            bodyAsText().toJsonObject().string("ident") shouldBe expectedIdent
        }
    }

    private fun ApplicationTestBuilder.createPersonaliaConsumer(): PersonaliaConsumer = PersonaliaConsumer(
        client = applicationHttpClient(),
        personaliaApiURL = URL(testhostBaseApi),
        personaliaTokendings = mockk<PersonaliaTokendings>().also {
            coEvery { it.exchangeToken(any()) } returns AccessToken("dummytoken")
        }
    )
}