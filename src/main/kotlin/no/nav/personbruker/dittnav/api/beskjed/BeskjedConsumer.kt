package no.nav.personbruker.dittnav.api.beskjed

import io.ktor.client.request.header
import io.ktor.client.request.request
import io.ktor.client.request.url
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import no.nav.personbruker.dittnav.api.config.Environment
import no.nav.personbruker.dittnav.api.config.HttpClientBuilder

class BeskjedConsumer(private val httpClientBuilder: HttpClientBuilder, private val environment: Environment) {

    suspend fun getExternalEvents(token: String): List<Beskjed> {
        val httpClient = httpClientBuilder.build()
        return httpClient.use { client ->
            client.request {
                url("${environment.dittNAVEventsURL}/fetch/beskjed")
                method = HttpMethod.Get
                header(HttpHeaders.Authorization, "Bearer $token")
            }
        }
    }
}
