package no.nav.personbruker.dittnav.api.loginstatus

import io.ktor.client.*
import no.nav.personbruker.dittnav.api.config.getWithEssoTokenHeader
import no.nav.personbruker.dittnav.common.security.AuthenticatedUser
import java.net.URL

class InnloggingsstatusConsumer(private val httpClient: HttpClient, private val baseUrl: URL) {

    suspend fun fetchAuthSummary(user: AuthenticatedUser): InnloggingsstatusResponse {
        return httpClient.getWithEssoTokenHeader(URL("$baseUrl/summary"), user)
    }
}