package no.nav.personbruker.dittnav.api.config

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.apache.Apache
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.header
import io.ktor.client.request.request
import io.ktor.client.request.url
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import no.nav.personbruker.dittnav.api.authentication.AuthenticatedUser
import java.net.URL

object HttpClientBuilder {

    fun build(httpClientEngine: HttpClientEngine = Apache.create()): HttpClient {
        return HttpClient(httpClientEngine) {
            install(ContentNegotiation) {
                json(jsonConfig())
            }
            install(HttpTimeout)
        }
    }

}

suspend inline fun <reified T> HttpClient.get(url: URL, user: AuthenticatedUser): T = withContext(Dispatchers.IO) {
    request {
        url(url)
        method = HttpMethod.Get
        header(HttpHeaders.Authorization, "Bearer ${user.token}")
    }.body()
}

suspend inline fun <reified T> HttpClient.get(url: URL, accessToken: String): T = withContext(Dispatchers.IO) {
    request {
        url(url)
        method = HttpMethod.Get
        header(HttpHeaders.Authorization, "Bearer $accessToken")
    }.body()
}

