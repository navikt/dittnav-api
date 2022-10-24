package no.nav.personbruker.dittnav.api.saker

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.timeout
import io.ktor.client.request.header
import io.ktor.client.request.request
import io.ktor.client.request.url
import io.ktor.http.HttpMethod
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import no.nav.personbruker.dittnav.api.tokenx.AccessToken
import no.nav.tms.token.support.tokendings.exchange.TokenXHeader
import java.net.URL

class MineSakerConsumer(
    private val client: HttpClient,
    mineSakerApiURL: URL
) {

    private val sisteEndredeSakerEndpoint = URL("$mineSakerApiURL/sakstemaer/sistendret")

    suspend fun hentSistEndret(user: AccessToken): SisteSakstemaerDTO =
        client.getWithTokenx(user).toInternal()

    private suspend fun HttpClient.getWithTokenx(accessToken: AccessToken): SisteSakstemaer =
        withContext(Dispatchers.IO) {
            request {
                url(sisteEndredeSakerEndpoint)
                method = HttpMethod.Get
                header(TokenXHeader.Authorization, "Bearer ${accessToken.value}")
                timeout {
                    socketTimeoutMillis = 30000
                    connectTimeoutMillis = 10000
                    requestTimeoutMillis = 40000
                }
            }.body()
        }
}