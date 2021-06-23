package no.nav.personbruker.dittnav.api.loginstatus

import io.ktor.client.*
import no.nav.personbruker.dittnav.api.config.getWithEssoTokenHeader
import no.nav.personbruker.dittnav.common.security.AuthenticatedUser
import java.net.URL

class InnloggingsstatusConsumer(private val httpClient: HttpClient, baseUrl: URL) {

    private val endpoint = URL("$baseUrl/summary")

    suspend fun fetchAuthSummary(user: AuthenticatedUser): InnloggingsstatusResponse {
        return httpClient.getWithEssoTokenHeader(endpoint, user)
    }
}
