package no.nav.personbruker.dittnav.api.done

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.respondError
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.statement.request
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import no.nav.personbruker.dittnav.api.common.InnloggetBrukerObjectMother
import no.nav.personbruker.dittnav.api.config.buildJsonSerializer
import org.amshove.kluent.`should equal`
import org.junit.jupiter.api.Test
import java.net.URL

internal class DoneProducerTest {

    val innloggetBruker = InnloggetBrukerObjectMother.createInnloggetBruker()

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
            doneProducer.postDoneEvents(done, innloggetBruker).status `should equal` HttpStatusCode.OK
            doneProducer.postDoneEvents(done, innloggetBruker).request.method `should equal` HttpMethod.Post
        }
    }
}
