package no.nav.personbruker.dittnav.api.done

import io.kotest.matchers.shouldBe
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import io.ktor.server.testing.testApplication
import io.mockk.coEvery
import io.mockk.mockk
import no.nav.personbruker.dittnav.api.TestUser
import no.nav.personbruker.dittnav.api.applicationHttpClient
import no.nav.personbruker.dittnav.api.assert
import no.nav.personbruker.dittnav.api.tokenx.EventaggregatorTokendings
import org.junit.jupiter.api.Test
import java.net.URL

internal class DoneProducerTest {

    @Test
    fun `should call post endpoint on event-aggregator`() {
        val user = TestUser.createAuthenticatedUser()
        val testEventAggregatorUrl = "http://event-aggregator"
        val eventaggregatorTokendings = mockk<EventaggregatorTokendings>().apply {
            coEvery { exchangeToken(user) } returns "<access_token>"
        }

        val done = DoneDTO(eventId = "dummyEventId")
        testApplication {
            externalServices {
                hosts(testEventAggregatorUrl) {
                    routing {
                        post("/beskjed/done") {
                            call.respond(HttpStatusCode.OK)
                        }
                    }
                }
            }
            DoneProducer(applicationHttpClient(), eventaggregatorTokendings, URL(testEventAggregatorUrl))
                .assert {
                    postDoneEvents(done, user).status shouldBe HttpStatusCode.OK
                }
        }
    }
}
