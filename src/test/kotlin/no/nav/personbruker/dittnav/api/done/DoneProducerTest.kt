package no.nav.personbruker.dittnav.api.done

import io.kotest.matchers.shouldBe
import io.ktor.client.statement.request
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.testApplication
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import no.nav.personbruker.dittnav.api.common.AuthenticatedUserObjectMother
import no.nav.personbruker.dittnav.api.tokenx.AccessToken
import no.nav.personbruker.dittnav.api.tokenx.EventhandlerTokendings
import no.nav.personbruker.dittnav.api.util.applicationHttpClient
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
        val done = DoneDTO(eventId = "dummyEventId")
        testApplication {
            val client =
                externalServices {
                    /* TODO
                }
                    addHandler { request ->
                        if (request.url.encodedPath.contains("/produce/done") && request.url.host.contains("event-handler")) {
                            respond(
                                "Ok",
                                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                            )
                        } else {
                            respondError(HttpStatusCode.BadRequest)
                        }*/
                }

            val doneProducer = DoneProducer(applicationHttpClient(), eventhandlerTokendings, URL("http://event-handler"))
            runBlocking {
                doneProducer.postDoneEvents(done, user).status shouldBe HttpStatusCode.OK
                doneProducer.postDoneEvents(done, user).request.method shouldBe HttpMethod.Post
            }
        }
    }
}
