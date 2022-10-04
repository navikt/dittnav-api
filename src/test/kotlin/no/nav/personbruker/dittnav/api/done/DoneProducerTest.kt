package no.nav.personbruker.dittnav.api.done

import io.kotest.matchers.shouldBe
import io.ktor.client.statement.request
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import io.ktor.server.testing.testApplication
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import no.nav.personbruker.dittnav.api.authentication.AuthenticatedUserObjectMother
import no.nav.personbruker.dittnav.api.tokenx.AccessToken
import no.nav.personbruker.dittnav.api.tokenx.EventhandlerTokendings
import no.nav.personbruker.dittnav.api.applicationHttpClient
import org.junit.jupiter.api.Test
import java.net.URL

internal class DoneProducerTest {

    @Test
    fun `should call post endpoint on event handler`() {
        val user = AuthenticatedUserObjectMother.createAuthenticatedUser()
        val testEventHandler = "http://event-handler"
        val eventhandlerTokendings = mockk<EventhandlerTokendings>()

        coEvery { eventhandlerTokendings.exchangeToken(user) } returns AccessToken("<access_token>")
        val done = DoneDTO(eventId = "dummyEventId")
        testApplication {
                externalServices {
                    hosts(testEventHandler){
                        routing {
                            post("/produce/done"){
                                call.respond(HttpStatusCode.OK)
                            }
                        }
                    }
                }
            val doneProducer = DoneProducer(applicationHttpClient(), eventhandlerTokendings, URL(testEventHandler))
            runBlocking {
                doneProducer.postDoneEvents(done, user).status shouldBe HttpStatusCode.OK
                doneProducer.postDoneEvents(done, user).request.method shouldBe HttpMethod.Post
            }
        }
    }
}
