package no.nav.personbruker.dittnav.api.oppgave

import io.ktor.client.HttpClient
import no.nav.personbruker.dittnav.api.config.getWithTokenx
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

    private suspend fun getExternalEvents(accessToken: AccessToken, comletePathToEndpoint: URL): List<Oppgave> {
        return client.getWithTokenx(comletePathToEndpoint, accessToken)
    }
}
