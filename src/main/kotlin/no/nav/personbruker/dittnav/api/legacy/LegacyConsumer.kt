package no.nav.personbruker.dittnav.api.legacy

import io.ktor.client.*
import io.ktor.client.statement.*
import io.ktor.http.*
import no.nav.personbruker.dittnav.api.config.getExtendedTimeout
import no.nav.personbruker.dittnav.api.saker.SakstemaDTO
import no.nav.personbruker.dittnav.common.security.AuthenticatedUser
import org.slf4j.LoggerFactory
import java.net.URL

class LegacyConsumer(private val httpClient: HttpClient, private val dittNAVLegacyBaseURL: URL) {

    private val log = LoggerFactory.getLogger(LegacyConsumer::class.java)

    private val legacyApiEndpoints: Map<LegacyApiOperations, URL>

    init {
        legacyApiEndpoints = mutableMapOf()
        LegacyApiOperations.values().forEach { operation ->
            val currentCompleteEndpoint = URL("$dittNAVLegacyBaseURL${operation.path}")
            legacyApiEndpoints.put(operation, currentCompleteEndpoint)
        }
        validateCompleteEndpointsForAllOperations()
    }

    private fun validateCompleteEndpointsForAllOperations() {
        if (LegacyApiOperations.values().size != legacyApiEndpoints.size) {
            throw IllegalStateException("Det må finnes komplette endepunkter for alle operasjoner.")
        }
    }

    suspend fun getLegacyContent(operation: LegacyApiOperations, user: AuthenticatedUser): HttpResponse {
        val endpoint = legacyApiEndpoints[operation]
            ?: throw IllegalStateException("Fant ikke komplett endepunkt for operasjonen $operation")
        val response: HttpResponse = httpClient.getExtendedTimeout(endpoint, user)
        logContextInCaseOfErrors(response, operation, user)
        return response
    }

    suspend fun hentSisteEndret(user: AuthenticatedUser): List<SakstemaDTO> {
        val operation = LegacyApiOperations.SAKSTEMA
        val endpoint = legacyApiEndpoints[operation]
            ?: throw IllegalStateException("Fant ikke komplett endepunkt for operasjonen $operation")
        val externals = httpClient.getExtendedTimeout<LegacySakstemaerRespons>(endpoint, user)
        val internals = externals.toInternal()
        return plukkUtDeToSomErSistEndretFraSortertListe(internals)
    }

    private fun plukkUtDeToSomErSistEndretFraSortertListe(internals: List<SakstemaDTO>) =
        internals.subList(0, 2)

    private fun logContextInCaseOfErrors(
        response: HttpResponse,
        endpoint: LegacyApiOperations,
        user: AuthenticatedUser
    ) {
        if (enFeilOppstod(response)) {
            when {
                feiletMedHttpStatus401PgaUtloptToken(response, user) -> {
                    log.info("Token-et utløp mens request-en pågikk. Feil mot $dittNAVLegacyBaseURL${endpoint.path}: ${response.status.value} ${response.status.description}, $user.")

                }
                feiletMedHttpStatus408PgaTimeout(response) -> {
                    log.info("Det oppstod en timeout ved henting av $dittNAVLegacyBaseURL${endpoint.path}: ${response.status.value} ${response.status.description}, $user.")

                }
                else -> {
                    log.warn("Feil mot $dittNAVLegacyBaseURL${endpoint.path}: ${response.status.value} ${response.status.description}, $user.")
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
