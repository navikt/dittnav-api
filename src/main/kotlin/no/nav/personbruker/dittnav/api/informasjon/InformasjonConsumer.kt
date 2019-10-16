package no.nav.personbruker.dittnav.api.informasjon

import io.ktor.client.HttpClient
import io.ktor.client.request.header
import io.ktor.client.request.request
import io.ktor.client.request.url
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.auth.HttpAuthHeader
import no.nav.personbruker.dittnav.api.config.Environment

class InformasjonConsumer(private val httpClient: HttpClient, private val environment: Environment) {

    suspend fun getEvents(authHeader: HttpAuthHeader?): List<Informasjon> {
        return httpClient.use { client ->
            client.request {
                url(environment.dittNAVEventsURL + "fetch/informasjon")
                method = HttpMethod.Get
                header(HttpHeaders.Authorization, authHeader)
            }
        }
    }

}
