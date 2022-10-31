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
import no.nav.personbruker.dittnav.api.authentication.AuthenticatedUser
import java.net.URL

class MeldekortConsumer(
    private val client: HttpClient,
    private val meldekortTokendings: MeldekortTokendings,
    meldekortApiBaseURL: URL,
) {

    private val meldekortStatusEndpoint = URL("$meldekortApiBaseURL/api/person/meldekortstatus")

    suspend fun getMeldekortInfo(user: AuthenticatedUser): Meldekortinfo {
        val token = meldekortTokendings.exchangeToken(user)
        return getMeldekortStatus(token).toInternal()
    }

    suspend fun getMeldekortStatus(user: AuthenticatedUser): MeldekortstatusExternal {
        val token = meldekortTokendings.exchangeToken(user)

        return getMeldekortStatus(token)
    }


    suspend fun getMeldekortStatus(accessToken: String): MeldekortstatusExternal {
        return client.getWithMeldekortTokenx(meldekortStatusEndpoint, accessToken)
    }
}

private suspend inline fun <reified T> HttpClient.getWithMeldekortTokenx(url: URL, accessToken: String): T =
    withContext(Dispatchers.IO) {
        request {
            url(url)
            method = HttpMethod.Get
            header("TokenXAuthorization", "Bearer $accessToken")
            timeout {
                socketTimeoutMillis = 30000
                connectTimeoutMillis = 10000
                requestTimeoutMillis = 40000
            }
        }.body()
    }