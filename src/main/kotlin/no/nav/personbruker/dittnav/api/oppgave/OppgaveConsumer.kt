package no.nav.personbruker.dittnav.api.oppgave

import io.ktor.client.HttpClient
import no.nav.personbruker.dittnav.api.common.retryOnConnectionClosed
import no.nav.personbruker.dittnav.api.config.get
import no.nav.personbruker.dittnav.api.tokenx.AccessToken
import java.net.URL

class OppgaveConsumer(
        private val client: HttpClient,
        eventHandlerBaseURL: URL,
) {

    private val inactiveEventsEndpoint = URL("$eventHandlerBaseURL/fetch/oppgave/inaktive")
    private val activeEventsEndpoint = URL("$eventHandlerBaseURL/fetch/oppgave/aktive")

    suspend fun getExternalActiveEvents(accessToken: AccessToken): List<Oppgave> {
        return getExternalEvents(accessToken, activeEventsEndpoint)
    }

    suspend fun getExternalInactiveEvents(accessToken: AccessToken): List<Oppgave> {
        return getExternalEvents(accessToken, inactiveEventsEndpoint)
    }

    private suspend fun getExternalEvents(accessToken: AccessToken, completePathToEndpoint: URL): List<Oppgave> {
        return retryOnConnectionClosed {
            client.get(completePathToEndpoint, accessToken)
        }
    }
}
