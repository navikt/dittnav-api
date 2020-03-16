package no.nav.personbruker.dittnav.api.legacy

import io.ktor.client.HttpClient
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import no.nav.personbruker.dittnav.api.common.InnloggetBruker
import no.nav.personbruker.dittnav.api.config.get
import no.nav.personbruker.dittnav.api.config.getExtendedTimeout
import org.slf4j.LoggerFactory
import java.net.URL

class LegacyConsumer(private val httpClient: HttpClient, private val dittNAVLegacyBaseURL: URL) {

    private val log = LoggerFactory.getLogger(LegacyConsumer::class.java)

    suspend fun getLegacyContent(path: String, innloggetBruker: InnloggetBruker): HttpResponse {
        val endpoint = URL("$dittNAVLegacyBaseURL$path")
        val response: HttpResponse = httpClient.getExtendedTimeout(endpoint, innloggetBruker)
        if (response.status != HttpStatusCode.OK) {
            log.warn("Feil mot $dittNAVLegacyBaseURL$path: ${response.status.value} ${response.status.description}")
        }
        return response
    }
}
