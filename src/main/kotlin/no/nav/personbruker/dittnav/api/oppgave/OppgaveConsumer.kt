package no.nav.personbruker.dittnav.api.oppgave

import io.ktor.client.request.header
import io.ktor.client.request.request
import io.ktor.client.request.url
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import no.nav.personbruker.dittnav.api.config.Environment
import no.nav.personbruker.dittnav.api.config.HttpClientBuilder

class OppgaveConsumer(private val httpClientBuilder: HttpClientBuilder, private val environment: Environment) {

    suspend fun getExternalEvents(token: String): List<Oppgave> {
        val httpClient = httpClientBuilder.build()
        return httpClient.use { client ->
            client.request {
                url("${environment.dittNAVEventsURL}/fetch/oppgave")
                method = HttpMethod.Get
                header(HttpHeaders.Authorization, "Bearer $token")
            }
        }
    }
}
