package no.nav.personbruker.dittnav.api.legacy

import io.ktor.client.HttpClient
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import no.nav.personbruker.dittnav.api.config.get
import org.slf4j.LoggerFactory
import java.net.URL

class LegacyConsumer(private val httpClient: HttpClient, private val dittNAVLegacyBaseURL: URL) {

    private val log = LoggerFactory.getLogger(LegacyConsumer::class.java)

    suspend fun getLegacyContent(path: String, token: String): HttpResponse {
        val endpoint = URL("$dittNAVLegacyBaseURL$path")
        log.info("Skal hente fra: $endpoint")
        val response: HttpResponse = httpClient.get(endpoint, token)
        if (response.status != HttpStatusCode.OK) {
            log.warn("Error mot $dittNAVLegacyBaseURL$path: ${response.status.value} ${response.status.description}")
        }
        return response
    }
}
