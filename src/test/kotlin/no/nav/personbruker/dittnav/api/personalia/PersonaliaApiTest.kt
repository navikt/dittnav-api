package no.nav.personbruker.dittnav.api.personalia

import com.expediagroup.graphql.client.ktor.GraphQLKtorClient
import io.kotest.matchers.shouldBe
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.server.testing.testApplication
import io.mockk.coEvery
import io.mockk.mockk
import no.nav.personbruker.dittnav.api.authenticatedGet
import no.nav.personbruker.dittnav.api.mockApi
import no.nav.personbruker.dittnav.api.string
import no.nav.personbruker.dittnav.api.toJsonObject
import no.nav.personbruker.dittnav.api.tokenx.AccessToken
import org.junit.jupiter.api.Test
import java.net.URL

class PersonaliaApiTest {
    private val testhostBaseApi = "https://personalia.test"

    @Test
    fun `personalia med ident`() = testApplication {
        mockApi(personaliaService = createPersonaliaService())
        val expectedIdent = "subject"

        client.authenticatedGet("dittnav-api/ident").apply {
            status shouldBe HttpStatusCode.OK
            bodyAsText().toJsonObject().string("ident") shouldBe expectedIdent
        }
    }

    private fun ApplicationTestBuilder.createPersonaliaService(): PersonaliaService = PersonaliaService(
        personaliaConsumer = PersonaliaConsumer(
            client = GraphQLKtorClient(URL("$testhostBaseApi/graphql")),
            pdlUrl = "$testhostBaseApi/graphql"
        ), personaliaTokendings = mockk<PersonaliaTokendings>().also {
            coEvery { it.exchangeToken(any()) } returns AccessToken("dummytoken")
        }
    )

}

