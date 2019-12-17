package no.nav.personbruker.dittnav.api.legacy

import io.ktor.client.HttpClient
import io.ktor.client.response.HttpResponse
import no.nav.personbruker.dittnav.api.config.Environment
import no.nav.personbruker.dittnav.api.config.get
import org.slf4j.LoggerFactory

class LegacyConsumer(private val httpClient: HttpClient, private val environment: Environment) {

    private val log = LoggerFactory.getLogger(LegacyConsumer::class.java)

    suspend fun getLegacyContent(url: String, token: String): HttpResponse {
        val response: HttpResponse = httpClient.get("${environment.dittNAVLegacyURL}$url", token)
        log.info("Mottok følgende respons: $response")
        return response
    }
}
