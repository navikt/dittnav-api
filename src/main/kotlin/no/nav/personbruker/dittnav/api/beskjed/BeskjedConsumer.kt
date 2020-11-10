package no.nav.personbruker.dittnav.api.beskjed

import io.ktor.client.*
import no.nav.personbruker.dittnav.api.config.get
import no.nav.personbruker.dittnav.common.security.AuthenticatedUser
import java.net.URL

class BeskjedConsumer(
        private val client: HttpClient,
        private val eventHandlerBaseURL: URL,
        private val pathToEndpoint: URL = URL("$eventHandlerBaseURL/fetch/beskjed")
) {

    suspend fun getExternalActiveEvents(user: AuthenticatedUser): List<Beskjed> {
        val completePathToEndpoint = URL("$pathToEndpoint/aktive")
        return getExternalEvents(user, completePathToEndpoint)
    }

    suspend fun getExternalInactiveEvents(user: AuthenticatedUser): List<Beskjed> {
        val completePathToEndpoint = URL("$pathToEndpoint/inaktive")
        return getExternalEvents(user, completePathToEndpoint)
    }

    private suspend fun getExternalEvents(user: AuthenticatedUser, completePathToEndpoint: URL): List<Beskjed> {
        return client.get(completePathToEndpoint, user)
    }
}
