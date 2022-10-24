package no.nav.personbruker.dittnav.api.meldekort

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
import java.net.URL

class MeldekortConsumer(
    private val client: HttpClient,
    meldekortApiBaseURL: URL,
) {

    private val meldekortStatusEndpoint = URL("$meldekortApiBaseURL/api/person/meldekortstatus")

    suspend fun getMeldekortStatus(accessToken: AccessToken): MeldekortstatusExternal {
        return client.getWithMeldekortTokenx(meldekortStatusEndpoint, accessToken)
    }
}

private suspend inline fun <reified T> HttpClient.getWithMeldekortTokenx(url: URL, accessToken: AccessToken): T =
    withContext(Dispatchers.IO) {
        request {
            url(url)
            method = HttpMethod.Get
            header("TokenXAuthorization", "Bearer ${accessToken.value}")
            timeout {
                socketTimeoutMillis = 30000
                connectTimeoutMillis = 10000
                requestTimeoutMillis = 40000
            }
        }.body()
    }