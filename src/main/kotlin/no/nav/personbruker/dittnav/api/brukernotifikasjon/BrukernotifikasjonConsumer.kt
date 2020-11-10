package no.nav.personbruker.dittnav.api.brukernotifikasjon

import io.ktor.client.HttpClient
import no.nav.personbruker.dittnav.api.config.get
import no.nav.personbruker.dittnav.common.security.AuthenticatedUser
import java.net.URL

class BrukernotifikasjonConsumer(
        private val client: HttpClient,
        private val eventHandlerBaseURL: URL,
        private val pathToEndpoint: URL = URL("$eventHandlerBaseURL/count/brukernotifikasjoner")
) {

    suspend fun countInactive(user: AuthenticatedUser): Int {
        val completePathToEndpoint = URL("$pathToEndpoint/inactive")
        return client.get(completePathToEndpoint, user)
    }

    suspend fun countActive(user: AuthenticatedUser): Int {
        val completePathToEndpoint = URL("$pathToEndpoint/active")
        return client.get(completePathToEndpoint, user)
    }

    suspend fun count(user: AuthenticatedUser): Int {
        val completePathToEndpoint = URL("$pathToEndpoint")
        return client.get(completePathToEndpoint, user)
    }

}
