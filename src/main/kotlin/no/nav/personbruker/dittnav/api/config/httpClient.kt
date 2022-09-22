package no.nav.personbruker.dittnav.api.config

import io.ktor.client.HttpClient
import io.ktor.client.features.timeout
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import no.nav.personbruker.dittnav.api.authentication.AuthenticatedUser
import no.nav.personbruker.dittnav.api.done.DoneDTO
import no.nav.personbruker.dittnav.api.tokenx.AccessToken
import no.nav.tms.token.support.tokendings.exchange.TokenXHeader
import java.net.URL

const val consumerIdHeaderName = "Nav-Consumer-Id"
const val consumerIdHeaderValue = "min-side:dittnav-api"

suspend inline fun <reified T> HttpClient.get(url: URL, user: AuthenticatedUser): T = withContext(Dispatchers.IO) {
    request {
        url(url)
        method = HttpMethod.Get
        header(HttpHeaders.Authorization, user.createAuthenticationHeader())
    }
}

suspend inline fun <reified T> HttpClient.get(url: URL, accessToken: AccessToken): T = withContext(Dispatchers.IO) {
    request {
        url(url)
        method = HttpMethod.Get
        header(HttpHeaders.Authorization, "Bearer ${accessToken.value}")
    }
}

suspend inline fun <reified T> HttpClient.getWithConsumerId(url: URL, accessToken: AccessToken): T = withContext(Dispatchers.IO) {
    request {
        url(url)
        method = HttpMethod.Get
        header(HttpHeaders.Authorization, "Bearer ${accessToken.value}")
        header(consumerIdHeaderName, consumerIdHeaderValue)
    }
}

suspend inline fun <reified T> HttpClient.getWithTokenx(url: URL, accessToken: AccessToken): T = withContext(Dispatchers.IO) {
    request {
        url(url)
        method = HttpMethod.Get
        header(TokenXHeader.Authorization, "Bearer ${accessToken.value}")
        timeout {
            socketTimeoutMillis = 30000
            connectTimeoutMillis = 10000
            requestTimeoutMillis = 40000
        }
    }
}

suspend inline fun <reified T> HttpClient.getWithMeldekortTokenx(url: URL, accessToken: AccessToken): T = withContext(Dispatchers.IO) {
    request {
        url(url)
        method = HttpMethod.Get
        header("TokenXAuthorization", "Bearer ${accessToken.value}")
        timeout {
            socketTimeoutMillis = 30000
            connectTimeoutMillis = 10000
            requestTimeoutMillis = 40000
        }
    }
}


suspend inline fun <reified T> HttpClient.post(url: URL, done: DoneDTO, accessToken: AccessToken): T = withContext(Dispatchers.IO) {
    post {
        url(url)
        method = HttpMethod.Post
        header(HttpHeaders.Authorization, "Bearer ${accessToken.value}")
        contentType(ContentType.Application.Json)
        body = done
    }
}
