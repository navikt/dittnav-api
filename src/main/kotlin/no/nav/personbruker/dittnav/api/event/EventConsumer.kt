package no.nav.personbruker.dittnav.api.event

import io.ktor.client.HttpClient
import io.ktor.client.request.header
import io.ktor.client.request.request
import io.ktor.client.request.url
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import no.nav.personbruker.dittnav.api.config.Environment

class EventConsumer(private val httpClient: HttpClient, private val environment: Environment) {

    suspend fun getEvents(token: String): List<Event> {
        return httpClient.use { client ->
            client.request {
                url(environment.dittNAVEventsURL + "fetch/informasjon")
                method = HttpMethod.Get
                header(HttpHeaders.Authorization, "Bearer $token")
            }
        }
    }
}
