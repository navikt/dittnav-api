package no.nav.personbruker.dittnav.api.legacy

import io.ktor.client.request.header
import io.ktor.client.request.request
import io.ktor.client.request.url
import io.ktor.client.response.HttpResponse
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import no.nav.personbruker.dittnav.api.config.Environment
import no.nav.personbruker.dittnav.api.config.HttpClientBuilder
import org.slf4j.LoggerFactory

class LegacyConsumer(private val httpClientBuilder: HttpClientBuilder, private val environment: Environment) {

    private val log = LoggerFactory.getLogger(LegacyConsumer::class.java)

    suspend fun getLegacyContent(url: String, token: String): HttpResponse {
        val httpClient = httpClientBuilder.build()
        val response: HttpResponse = httpClient.use { client ->
            client.request {
                url(environment.dittNAVLegacyURL.toString() + url)
                method = HttpMethod.Get
                header(HttpHeaders.Authorization, "Bearer $token")
            }
        }
        log.info("Mottok følgende respons: $response")
        return response
    }
}
