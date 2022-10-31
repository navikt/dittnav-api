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
import no.nav.personbruker.dittnav.api.authentication.AuthenticatedUser
import no.nav.personbruker.dittnav.api.config.ConsumeSakerException
import no.nav.tms.token.support.tokendings.exchange.TokenXHeader
import java.net.URL

class MineSakerConsumer(
    private val client: HttpClient,
    private val mineSakerUrl: URL,
    private val mineSakerTokendings: MineSakerTokendings,
    mineSakerApiURL: URL
) {

    private val sisteEndredeSakerEndpoint = URL("$mineSakerApiURL/sakstemaer/sistendret")
    suspend fun hentSistEndredeSakstemaer(user: AuthenticatedUser): SakerDTO {
        return try {
            val exchangedToken = mineSakerTokendings.exchangeToken(user)
            val sisteSakstemaer = client.getWithTokenx(exchangedToken).toInternal()
            SakerDTO(sisteSakstemaer.sakstemaer, mineSakerUrl, sisteSakstemaer.dagpengerSistEndret)
        } catch (e: Exception) {
            throw ConsumeSakerException("Klarte ikke å hente info om saker", e)
        }
    }

    private suspend fun HttpClient.getWithTokenx(accessToken: String): SisteSakstemaer =
        withContext(Dispatchers.IO) {
            request {
                url(sisteEndredeSakerEndpoint)
                method = HttpMethod.Get
                header(TokenXHeader.Authorization, "Bearer $accessToken")
                timeout {
                    socketTimeoutMillis = 30000
                    connectTimeoutMillis = 10000
                    requestTimeoutMillis = 40000
                }
            }.body()
        }
}