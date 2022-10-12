package no.nav.personbruker.dittnav.api.innboks

import io.ktor.client.*
import no.nav.personbruker.dittnav.api.common.retryOnConnectionClosed
import no.nav.personbruker.dittnav.api.config.get
import no.nav.personbruker.dittnav.api.tokenx.AccessToken
import java.net.URL

class InnboksConsumer(
        private val client: HttpClient,
        eventHandlerBaseURL: URL
) {

    private val activeEventsEndpoint = URL("$eventHandlerBaseURL/fetch/innboks/aktive")
    private val inactiveEventsEndpoint = URL("$eventHandlerBaseURL/fetch/innboks/inaktive")

    suspend fun getExternalActiveEvents(accessToken: AccessToken): List<Innboks> {
        return getExternalEvents(accessToken, activeEventsEndpoint)
    }

    suspend fun getExternalInactiveEvents(accessToken: AccessToken): List<Innboks> {
        return getExternalEvents(accessToken, inactiveEventsEndpoint)
    }

    private suspend fun getExternalEvents(accessToken: AccessToken, completePathToEndpoint: URL): List<Innboks> {
        return retryOnConnectionClosed {
            client.get(completePathToEndpoint, accessToken)
        }
    }
}
