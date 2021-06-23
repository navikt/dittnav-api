package no.nav.personbruker.dittnav.api.innboks

import io.ktor.client.HttpClient
import no.nav.personbruker.dittnav.api.config.get
import no.nav.personbruker.dittnav.common.security.AuthenticatedUser
import java.net.URL

class InnboksConsumer(
        private val client: HttpClient,
        eventHandlerBaseURL: URL
) {

    private val activeEventsEndpoint = URL("$eventHandlerBaseURL/fetch/innboks/aktive")
    private val inactiveEventsEndpoint = URL("$eventHandlerBaseURL/fetch/innboks/inaktive")

    suspend fun getExternalActiveEvents(user: AuthenticatedUser): List<Innboks> {
        return getExternalEvents(user, activeEventsEndpoint)
    }

    suspend fun getExternalInactiveEvents(user: AuthenticatedUser): List<Innboks> {
        return getExternalEvents(user, inactiveEventsEndpoint)
    }

    private suspend fun getExternalEvents(user: AuthenticatedUser, completePathToEndpoint: URL): List<Innboks> {
        return client.get(completePathToEndpoint, user)
    }
}
