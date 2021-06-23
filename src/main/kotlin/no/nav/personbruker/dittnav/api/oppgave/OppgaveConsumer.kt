package no.nav.personbruker.dittnav.api.oppgave

import io.ktor.client.HttpClient
import no.nav.personbruker.dittnav.api.config.get
import no.nav.personbruker.dittnav.common.security.AuthenticatedUser
import java.net.URL

class OppgaveConsumer(
        private val client: HttpClient,
        eventHandlerBaseURL: URL,
) {

    private val inactiveEventsEndpoint = URL("$eventHandlerBaseURL/fetch/oppgave/inaktive")
    private val activeEventsEndpoint = URL("$eventHandlerBaseURL/fetch/oppgave/aktive")

    suspend fun getExternalActiveEvents(user: AuthenticatedUser): List<Oppgave> {
        return getExternalEvents(user, activeEventsEndpoint)
    }

    suspend fun getExternalInactiveEvents(user: AuthenticatedUser): List<Oppgave> {
        return getExternalEvents(user, inactiveEventsEndpoint)
    }

    private suspend fun getExternalEvents(user: AuthenticatedUser, comletePathToEndpoint: URL): List<Oppgave> {
        return client.get(comletePathToEndpoint, user)
    }
}
