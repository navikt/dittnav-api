package no.nav.personbruker.dittnav.api.oppgave

import io.kotest.matchers.shouldBe
import io.ktor.server.testing.testApplication
import kotlinx.coroutines.runBlocking
import no.nav.personbruker.dittnav.api.tokenx.AccessToken
import no.nav.personbruker.dittnav.api.util.applicationHttpClient
import org.junit.jupiter.api.Test
import java.net.URL

internal class OppgaveConsumerTest {

    private val dummyToken = AccessToken("<access_token>")

    @Test
    fun `should call oppgave endpoint on event handler`() {


        testApplication {
            val client = applicationHttpClient()
            val oppgaveConsumer = OppgaveConsumer(client, URL("http://event-handler"))
            externalServices {
                //TODO
                /*   addHandler { request ->
                       if (request.url.encodedPath.contains("/fetch/oppgave") && request.url.host.contains("event-handler")) {
                           respond("[]", headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString()))
                       } else {
                           respondError(HttpStatusCode.BadRequest)
                       }
                   }*/
            }

            runBlocking {
                oppgaveConsumer.getExternalActiveEvents(dummyToken) shouldBe emptyList()
            }
        }
    }

    @Test
    fun `should get list of active Oppgave`() {
        val oppgaveObject1 = createOppgave("1", "1", true)
        val oppgaveObject2 = createOppgave("2", "2", true)

        testApplication {
            val client = applicationHttpClient()
            externalServices {
                //TODO
                /*       jsonConfig().encodeToString(listOf(oppgaveObject1, oppgaveObject2)),
       headers = headersOf(HttpHeaders.ContentType,
           ContentType.Application.Json.toString())*/
            }


            val oppgaveConsumer = OppgaveConsumer(client, URL("http://event-handler"))

            runBlocking {
                val externalActiveEvents = oppgaveConsumer.getExternalActiveEvents(dummyToken)
                val event = externalActiveEvents.first()
                externalActiveEvents.size shouldBe 2
                event.tekst shouldBe oppgaveObject1.tekst
                event.fodselsnummer shouldBe oppgaveObject1.fodselsnummer
                event.aktiv shouldBe true
            }
        }
    }

    @Test
    fun `should get list of inactive Oppgave`() {
        val oppgaveObject = createOppgave("1", "1", false)

        testApplication {
            val client = applicationHttpClient()
            externalServices {
                /*      jsonConfig().encodeToString(listOf(oppgaveObject)),
                      headers = headersOf(
                          HttpHeaders.ContentType,
                          ContentType.Application.Json.toString()
                      )*/
            }

            val oppgaveConsumer = OppgaveConsumer(client, URL("http://event-handler"))

            runBlocking {
                val externalInactiveEvents = oppgaveConsumer.getExternalInactiveEvents(dummyToken)
                val event = externalInactiveEvents.first()
                externalInactiveEvents.size shouldBe 1
                event.tekst shouldBe oppgaveObject.tekst
                event.fodselsnummer shouldBe oppgaveObject.fodselsnummer
                event.aktiv shouldBe false
            }
        }
    }
}
