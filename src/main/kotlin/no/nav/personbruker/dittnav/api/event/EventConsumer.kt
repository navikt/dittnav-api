package no.nav.personbruker.dittnav.api.event

import io.ktor.client.request.header
import io.ktor.client.request.request
import io.ktor.client.request.url
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import no.nav.personbruker.dittnav.api.config.Environment
import no.nav.personbruker.dittnav.api.config.HttpClientBuilder
import org.slf4j.LoggerFactory

class EventConsumer(private val httpClientBuilder: HttpClientBuilder, private val environment: Environment) {

    private val log = LoggerFactory.getLogger(EventConsumer::class.java)

    suspend fun getEvents(token: String): List<Event> {
        val httpClient = httpClientBuilder.build()
        val eventer: List<Event> = httpClient.use { client ->
            client.request {
                url(environment.dittNAVEventsURL + "fetch/informasjon")
                method = HttpMethod.Get
                header(HttpHeaders.Authorization, "Bearer $token")
            }
        }
        log.info("Hentet ${eventer.size}, fant følgende enventer:\n$eventer")
        return eventer
    }

}
