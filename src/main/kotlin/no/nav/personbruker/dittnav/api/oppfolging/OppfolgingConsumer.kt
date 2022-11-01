package no.nav.personbruker.dittnav.api.oppfolging

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.request
import io.ktor.client.request.url
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import no.nav.personbruker.dittnav.api.authentication.AuthenticatedUser
import java.net.URL

const val consumerIdHeaderName = "Nav-Consumer-Id"
const val consumerIdHeaderValue = "min-side:dittnav-api"
class OppfolgingConsumer(
    private val client: HttpClient,
    oppfolgingApiBaseURL: URL,
) {
    private val oppfolgingStatusEndpoint = URL("$oppfolgingApiBaseURL/api/niva3/underoppfolging")

    suspend fun getOppfolging(user: AuthenticatedUser): Oppfolging =
        client.getWithConsumerId(user.token).toInternal()

    private suspend  fun HttpClient.getWithConsumerId(accessToken: String): OppfolgingExternal =
        withContext(Dispatchers.IO) {
            request {
                url(oppfolgingStatusEndpoint)
                method = HttpMethod.Get
                header(HttpHeaders.Authorization, "Bearer $accessToken")
                header(consumerIdHeaderName, consumerIdHeaderValue)
            }.body()
        }
}
