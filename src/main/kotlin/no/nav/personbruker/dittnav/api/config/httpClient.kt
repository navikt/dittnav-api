package no.nav.personbruker.dittnav.api.config

import io.ktor.client.HttpClient
import io.ktor.client.request.header
import io.ktor.client.request.request
import io.ktor.client.request.url
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend inline fun <reified T> HttpClient.get(url: String, token: String): T = withContext(Dispatchers.IO) {
    request<T> {
        url(url)
        method = HttpMethod.Get
        header(HttpHeaders.Authorization, "Bearer $token")
    }
}
