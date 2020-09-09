package no.nav.personbruker.dittnav.api.legacy

import io.ktor.client.*
import io.ktor.client.statement.*
import io.ktor.http.*
import no.nav.personbruker.dittnav.api.common.InnloggetBruker
import no.nav.personbruker.dittnav.api.config.getExtendedTimeout
import org.slf4j.LoggerFactory
import java.net.URL

class LegacyConsumer(private val httpClient: HttpClient, private val dittNAVLegacyBaseURL: URL) {

    private val log = LoggerFactory.getLogger(LegacyConsumer::class.java)

    suspend fun getLegacyContent(path: String, innloggetBruker: InnloggetBruker): HttpResponse {
        val endpoint = URL("$dittNAVLegacyBaseURL$path")
        val response: HttpResponse = httpClient.getExtendedTimeout(endpoint, innloggetBruker)
        logContextInCaseOfErrors(response, path, innloggetBruker)
        return response
    }

    private fun logContextInCaseOfErrors(response: HttpResponse, path: String, innloggetBruker: InnloggetBruker) {
        if (response.status != HttpStatusCode.OK) {
            if (isFeiletMed401PgaUtloptToken(response, innloggetBruker)) {
                log.info("Token-et utløp mens request-en pågikk. Feil mot $dittNAVLegacyBaseURL$path: ${response.status.value} ${response.status.description}, $innloggetBruker.")

            } else {
                log.warn("Feil mot $dittNAVLegacyBaseURL$path: ${response.status.value} ${response.status.description}, $innloggetBruker.")
            }
        }
    }

    private fun isFeiletMed401PgaUtloptToken(response: HttpResponse, innloggetBruker: InnloggetBruker) =
        response.status == HttpStatusCode.Unauthorized && innloggetBruker.isTokenExpired()

}
