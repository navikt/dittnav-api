package no.nav.personbruker.dittnav.api.done

import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.features.json.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import no.nav.personbruker.dittnav.api.common.AuthenticatedUserObjectMother
import no.nav.personbruker.dittnav.api.config.buildJsonSerializer
import org.amshove.kluent.`should be equal to`
import org.junit.jupiter.api.Test
import java.net.URL

internal class DoneProducerTest {

    val user = AuthenticatedUserObjectMother.createAuthenticatedUser()

    @Test
    fun `should call post endpoint on event handler`() {
        val done = DoneObjectMother.createDone("dummyUid", "dummyEventId")

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
            install(JsonFeature) {
                serializer = buildJsonSerializer()
            }
        }
        val doneProducer = DoneProducer(client, URL("http://event-handler"))
        runBlocking {
            doneProducer.postDoneEvents(done, user).status `should be equal to` HttpStatusCode.OK
            doneProducer.postDoneEvents(done, user).request.method `should be equal to` HttpMethod.Post
        }
    }
}
