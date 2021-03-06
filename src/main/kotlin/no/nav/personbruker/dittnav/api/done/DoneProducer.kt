package no.nav.personbruker.dittnav.api.done

import io.ktor.client.HttpClient
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import no.nav.personbruker.dittnav.common.security.AuthenticatedUser
import org.slf4j.LoggerFactory
import java.net.URL

class DoneProducer(private val httpClient: HttpClient, dittNAVBaseURL: URL) {

    private val log = LoggerFactory.getLogger(DoneProducer::class.java)
    private val completePathToEndpoint = URL("$dittNAVBaseURL/produce/done")

    suspend fun postDoneEvents(done: DoneDto, user: AuthenticatedUser): HttpResponse {
        val response: HttpResponse = post(completePathToEndpoint, done, user)

        if (response.status != HttpStatusCode.OK) {
            log.warn("Feil mot $completePathToEndpoint: ${response.status.value} ${response.status.description}")
        }
        return response
    }

    private suspend inline fun <reified T> post(url: URL, done: DoneDto, user: AuthenticatedUser): T = withContext(Dispatchers.IO) {
        httpClient.post<T>() {
            url(url)
            method = HttpMethod.Post
            header(HttpHeaders.Authorization, user.createAuthenticationHeader())
            contentType(ContentType.Application.Json)
            body = done
        }
    }
}
