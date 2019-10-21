package no.nav.personbruker.dittnav.api.legacy

import io.ktor.client.HttpClient
import io.ktor.client.request.header
import io.ktor.client.request.request
import io.ktor.client.request.url
import io.ktor.client.response.HttpResponse
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.auth.HttpAuthHeader
import no.nav.personbruker.dittnav.api.config.Environment

class LegacyConsumer(private val httpClient: HttpClient, private val environment: Environment) {

    suspend fun getLegacyContent(url: String, authHeader: HttpAuthHeader?): HttpResponse {
        return httpClient.use { client ->
            client.request {
                url(environment.dittNAVLegacyURL + url)
                method = HttpMethod.Get
                header(HttpHeaders.Authorization, authHeader)
            }
        }
    }

}
