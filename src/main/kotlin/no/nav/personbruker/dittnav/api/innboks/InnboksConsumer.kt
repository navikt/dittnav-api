package no.nav.personbruker.dittnav.api.innboks

import io.ktor.client.HttpClient
import no.nav.personbruker.dittnav.api.config.get
import no.nav.personbruker.dittnav.common.security.AuthenticatedUser
import java.net.URL

class InnboksConsumer(
        private val client: HttpClient,
        private val eventHandlerBaseURL: URL,
        private val pathToEndpoint: URL = URL("$eventHandlerBaseURL/fetch/innboks")
) {

    suspend fun getExternalActiveEvents(user: AuthenticatedUser): List<Innboks> {
        val completePathToEndpoint = URL("$pathToEndpoint/aktive")
        val externalActiveEvents = getExternalEvents(user, completePathToEndpoint)
        return externalActiveEvents
    }

    suspend fun getExternalInactiveEvents(user: AuthenticatedUser): List<Innboks> {
        val completePathToEndpoint = URL("$pathToEndpoint/inaktive")
        val externalInactiveEvents = getExternalEvents(user, completePathToEndpoint)
        return externalInactiveEvents
    }

    private suspend fun getExternalEvents(user: AuthenticatedUser, completePathToEndpoint: URL): List<Innboks> {
        return client.get(completePathToEndpoint, user)
    }
}
