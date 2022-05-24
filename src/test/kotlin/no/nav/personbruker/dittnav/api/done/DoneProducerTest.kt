package no.nav.personbruker.dittnav.api.done

import io.kotest.matchers.shouldBe
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.respondError
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.statement.request
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import no.nav.personbruker.dittnav.api.common.AuthenticatedUserObjectMother
import no.nav.personbruker.dittnav.api.tokenx.AccessToken
import no.nav.personbruker.dittnav.api.tokenx.EventhandlerTokendings
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.net.URL

internal class DoneProducerTest {

    private val user = AuthenticatedUserObjectMother.createAuthenticatedUser()
    private val dummyToken = AccessToken("<access_token>")

    private val eventhandlerTokendings = mockk<EventhandlerTokendings>()

    @BeforeEach
    fun setup() {
        coEvery { eventhandlerTokendings.exchangeToken(user) } returns dummyToken
    }

    @Test
    fun `should call post endpoint on event handler`() {
        val done = DoneDtoObjectMother.createDoneDto(eventId = "dummyEventId")

        val client = HttpClient(MockEngine) {
            engine {
                addHandler { request ->
                    if (request.url.encodedPath.contains("/produce/done") && request.url.host.contains("event-handler")) {
                        respond("Ok", headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString()))
                    } else {
                        respondError(HttpStatusCode.BadRequest)
                    }
                }
            }
            install(JsonFeature)
        }
        val doneProducer = DoneProducer(client, eventhandlerTokendings, URL("http://event-handler"))
        runBlocking {
            doneProducer.postDoneEvents(done, user).status shouldBe HttpStatusCode.OK
            doneProducer.postDoneEvents(done, user).request.method shouldBe HttpMethod.Post
        }
    }
}
