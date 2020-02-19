package no.nav.personbruker.dittnav.api.done

import io.ktor.client.HttpClient
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.content.TextContent
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import no.nav.personbruker.dittnav.api.common.InnloggetBruker
import org.json.simple.JSONObject
import org.slf4j.LoggerFactory
import java.net.URL


class DoneProducer(private val httpClient: HttpClient, private val dittNAVBaseURL: URL) {

    private val log = LoggerFactory.getLogger(DoneProducer::class.java)
    private val handlerPath = "/handler/produce/done"

    suspend fun postDoneEvents(doneDTO: DoneDTO, innloggetBruker: InnloggetBruker): HttpResponse {
        val endpoint = URL("$dittNAVBaseURL$handlerPath")
        log.info("Sender POST fra: $endpoint")
        val response: HttpResponse = post(endpoint, innloggetBruker, doneDTO)

        if (response.status != HttpStatusCode.OK) {
            log.warn("Error mot $dittNAVBaseURL$handlerPath: ${response.status.value} ${response.status.description}")
        }
        return response
    }

    private suspend inline fun <reified T> post(url: URL, innloggetBruker: InnloggetBruker, done: DoneDTO): T = withContext(Dispatchers.IO) {
        httpClient.post<T>() {
            url(url)
            method = HttpMethod.Post
            header(HttpHeaders.Authorization, innloggetBruker.getBearerToken())
            val json = mapOf<String, String>("eventId" to done.eventId)
            body = TextContent(JSONObject.toJSONString(json), contentType = ContentType.Application.Json)
        }
    }
}
