package no.nav.personbruker.dittnav.api.personalia

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeSameInstanceAs
import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.server.testing.testApplication
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import no.nav.personbruker.dittnav.api.TestUser
import no.nav.personbruker.dittnav.api.applicationHttpClient
import no.nav.personbruker.dittnav.api.config.ConsumePersonaliaException
import no.nav.personbruker.dittnav.api.externalServiceWith500Response
import no.nav.personbruker.dittnav.api.externalServiceWithJsonResponse
import no.nav.personbruker.dittnav.api.tokenx.AccessToken
import org.junit.jupiter.api.Test
import java.net.URL

internal class PersonaliaConsumerTest {

    private val tokendings = mockk<PersonaliaTokendings>()
    private val dummyUser = TestUser.createAuthenticatedUser()
    private val dummyAccessToken = AccessToken("123")
    private val personaliaApiTestUrl = "http://personalia.test"
    //

    init {
        coEvery { tokendings.exchangeToken(any()) } returns dummyAccessToken
    }

    @Test
    fun `Skal hente navn fra personalia api`() =
        testApplication {
            externalServiceWithJsonResponse(
                hostApiBase = personaliaApiTestUrl,
                endpoint = "/navn",
                content = """{"navn": "Navnert Navnesen"}""".trimIndent()
            )
            personaliaConsumer().hentNavn(dummyUser).navn shouldBe "Navnert Navnesen"
        }


    @Test
    fun `Skal fange og kaste feil videre hvis henting av navn feiler`() =
        testApplication {
            externalServiceWith500Response(testHost = personaliaApiTestUrl, route = "/navn")
            shouldThrow<ConsumePersonaliaException> {  personaliaConsumer().hentNavn(dummyUser) }
        }


    @Test
    fun `Skal hente ident fra personalia api`() {
        testApplication {
            externalServiceWithJsonResponse(
                hostApiBase = personaliaApiTestUrl,
                endpoint = "/ident",
                content = """{"ident": "12345678910"}""".trimIndent()
            )
            personaliaConsumer().hentIdent(dummyUser).ident shouldBe "12345678910"
        }
    }

    @Test
    fun `Skal fange og kaste feil videre hvis henting av ident feiler`() = testApplication{
        externalServiceWith500Response(testHost = personaliaApiTestUrl, route = "/navn")
        shouldThrow<ConsumePersonaliaException> {  personaliaConsumer().hentIdent(dummyUser) }
    }


    private fun ApplicationTestBuilder.personaliaConsumer() = PersonaliaConsumer(
        client = applicationHttpClient(),
        personaliaTokendings = tokendings,
        personaliaApiURL = URL(personaliaApiTestUrl)
    )

}
