package no.nav.personbruker.dittnav.api.beskjed

import io.ktor.client.*
import no.nav.personbruker.dittnav.api.config.get
import no.nav.personbruker.dittnav.common.security.AuthenticatedUser
import java.net.URL

class BeskjedConsumer(
        private val client: HttpClient,
        eventHandlerBaseURL: URL
) {

    private val activeEventsEndpoint = URL("$eventHandlerBaseURL/fetch/beskjed/aktive")
    private val inactiveEventsEndpoint = URL("$eventHandlerBaseURL/fetch/beskjed/inaktive")

    suspend fun getExternalActiveEvents(user: AuthenticatedUser): List<Beskjed> {
        return getExternalEvents(user, activeEventsEndpoint)
    }

    suspend fun getExternalInactiveEvents(user: AuthenticatedUser): List<Beskjed> {
        return getExternalEvents(user, inactiveEventsEndpoint)
    }

    private suspend fun getExternalEvents(user: AuthenticatedUser, completePathToEndpoint: URL): List<Beskjed> {
        return client.get(completePathToEndpoint, user)
    }
}
