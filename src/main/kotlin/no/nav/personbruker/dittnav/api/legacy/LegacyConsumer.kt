package no.nav.personbruker.dittnav.api.legacy

import io.ktor.client.HttpClient
import io.ktor.client.request.header
import io.ktor.client.request.request
import io.ktor.client.request.url
import io.ktor.client.response.HttpResponse
import io.ktor.client.response.readBytes
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import no.nav.personbruker.dittnav.api.config.Environment

class LegacyConsumer(private val httpClient: HttpClient, private val environment: Environment) {

    suspend fun checkHeaderAndGetLegacyContent(url: String, authHeader: String?): Pair<HttpStatusCode, ByteArray> {
        val status: HttpStatusCode
        val message: ByteArray
        if (authHeader !== null) {
            val content = getLegacyContent(url, authHeader)
            status = content.status
            message = content.readBytes()
        }
        else {
            status = HttpStatusCode.Unauthorized
            message = ByteArray(0)
        }
        return Pair(status, message)
    }

    private suspend fun getLegacyContent(url: String, authHeader: String): HttpResponse {
        return httpClient.request {
            url(environment.dittNAVLegacyURL + url)
            method = HttpMethod.Get
            header(HttpHeaders.Authorization, authHeader)
        }
    }

}
