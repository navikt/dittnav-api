package no.nav.personbruker.dittnav.api.oppgave

import io.ktor.client.HttpClient
import io.ktor.client.request.header
import io.ktor.client.request.request
import io.ktor.client.request.url
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.auth.HttpAuthHeader
import no.nav.personbruker.dittnav.api.config.Environment

class OppgaveConsumer(private val httpClient: HttpClient, private val environment: Environment) {

    suspend fun getEvents(authHeader: HttpAuthHeader?): List<Oppgave> {
        return httpClient.use { client ->
            client.request {
                url(environment.dittNAVEventsURL + "fetch/oppgave")
                method = HttpMethod.Get
                header(HttpHeaders.Authorization, authHeader)
            }
        }
    }

}
