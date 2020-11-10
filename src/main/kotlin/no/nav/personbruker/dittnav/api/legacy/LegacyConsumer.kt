package no.nav.personbruker.dittnav.api.legacy

import io.ktor.client.*
import io.ktor.client.statement.*
import io.ktor.http.*
import no.nav.personbruker.dittnav.api.config.getExtendedTimeout
import no.nav.personbruker.dittnav.common.security.AuthenticatedUser
import org.slf4j.LoggerFactory
import java.net.URL

class LegacyConsumer(private val httpClient: HttpClient, private val dittNAVLegacyBaseURL: URL) {

    private val log = LoggerFactory.getLogger(LegacyConsumer::class.java)

    suspend fun getLegacyContent(path: String, user: AuthenticatedUser): HttpResponse {
        val endpoint = URL("$dittNAVLegacyBaseURL$path")
        val response: HttpResponse = httpClient.getExtendedTimeout(endpoint, user)
        logContextInCaseOfErrors(response, path, user)
        return response
    }

    private fun logContextInCaseOfErrors(response: HttpResponse, path: String, user: AuthenticatedUser) {
        if (enFeilOppstod(response)) {
            when {
                feiletMedHttpStatus401PgaUtloptToken(response, user) -> {
                    log.info("Token-et utløp mens request-en pågikk. Feil mot $dittNAVLegacyBaseURL$path: ${response.status.value} ${response.status.description}, $user.")

                }
                feiletMedHttpStatus408PgaTimeout(response) -> {
                    log.info("Det oppstod en timeout ved henting av $dittNAVLegacyBaseURL$path: ${response.status.value} ${response.status.description}, $innloggetBruker.")

                }
                else -> {
                    log.warn("Feil mot $dittNAVLegacyBaseURL$path: ${response.status.value} ${response.status.description}, $user.")
                }
            }
        }
    }

    private fun enFeilOppstod(response: HttpResponse) =
        response.status != HttpStatusCode.OK

    private fun feiletMedHttpStatus401PgaUtloptToken(response: HttpResponse, user: AuthenticatedUser) =
        response.status == HttpStatusCode.Unauthorized && user.isTokenExpired()

    private fun feiletMedHttpStatus408PgaTimeout(response: HttpResponse): Boolean =
        response.status == HttpStatusCode.RequestTimeout

}
