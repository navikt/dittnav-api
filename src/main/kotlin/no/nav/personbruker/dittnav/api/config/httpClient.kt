package no.nav.personbruker.dittnav.api.config

import io.ktor.client.HttpClient
import io.ktor.client.features.timeout
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import no.nav.personbruker.dittnav.api.done.DoneDTO
import no.nav.personbruker.dittnav.api.tokenx.AccessToken
import no.nav.personbruker.dittnav.common.security.AuthenticatedUser
import no.nav.tms.token.support.tokendings.exchange.TokenXHeader
import java.net.URL

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

suspend inline fun <reified T> HttpClient.getExtendedTimeout(url: URL, user: AuthenticatedUser): T = withContext(Dispatchers.IO) {
    request {
        url(url)
        method = HttpMethod.Get
        header(HttpHeaders.Authorization, user.createAuthenticationHeader())
        timeout {
            socketTimeoutMillis = 30000
            connectTimeoutMillis = 10000
            requestTimeoutMillis = 40000
        }
    }
}

suspend inline fun <reified T> HttpClient.getWithEssoTokenHeader(url: URL, user: AuthenticatedUser): T = withContext(Dispatchers.IO) {
    require(user.auxiliaryEssoToken != null) {
        "Prøvde å sette esso-token som header, men fant det ikke for innlogget bruker."
    }
    request {
        url(url)
        method = HttpMethod.Get
        header(HttpHeaders.Authorization, user.createAuthenticationHeader())
        header("nav-esso", user.auxiliaryEssoToken)
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
