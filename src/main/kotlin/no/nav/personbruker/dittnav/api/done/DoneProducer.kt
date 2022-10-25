package no.nav.personbruker.dittnav.api.done


import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import mu.KotlinLogging
import no.nav.personbruker.dittnav.api.authentication.AuthenticatedUser
import no.nav.personbruker.dittnav.api.tokenx.AccessToken
import no.nav.personbruker.dittnav.api.tokenx.EventhandlerTokendings

import java.net.URL

class DoneProducer(
    private val httpClient: HttpClient,
    private val eventhandlerTokendings: EventhandlerTokendings,
    dittNAVBaseURL: URL
) {

    private val log = KotlinLogging.logger { }
    private val completePathToEndpoint = URL("$dittNAVBaseURL/produce/done")

    suspend fun postDoneEvents(done: DoneDTO, user: AuthenticatedUser): HttpResponse {
        val exchangedToken = eventhandlerTokendings.exchangeToken(user)
        return httpClient.post(done, exchangedToken). also { response ->
            if (response.status != HttpStatusCode.OK) {
                log.warn("Feil mot $completePathToEndpoint: ${response.status.value} ${response.status.description}")
            }
        }
    }

    private suspend fun HttpClient.post(done: DoneDTO, accessToken: AccessToken) =
        withContext(Dispatchers.IO) {
            post {
                url(completePathToEndpoint)
                method = HttpMethod.Post
                header(HttpHeaders.Authorization, "Bearer ${accessToken.value}")
                contentType(ContentType.Application.Json)
                setBody(done)
            }
        }
}

@Serializable
data class DoneDTO(
    val eventId: String
)