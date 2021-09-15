package no.nav.personbruker.dittnav.api.beskjed

import io.ktor.client.*
import no.nav.personbruker.dittnav.api.config.get
import no.nav.personbruker.dittnav.api.tokenx.AccessToken
import java.net.URL

class BeskjedConsumer(
    private val client: HttpClient,
    eventHandlerBaseURL: URL
) {

    private val activeEventsEndpoint = URL("$eventHandlerBaseURL/fetch/beskjed/aktive")
    private val inactiveEventsEndpoint = URL("$eventHandlerBaseURL/fetch/beskjed/inaktive")

    suspend fun getExternalActiveEvents(accessToken: AccessToken): List<Beskjed> {
        return getExternalEvents(accessToken, activeEventsEndpoint)
    }

    suspend fun getExternalInactiveEvents(accessToken: AccessToken): List<Beskjed> {
        return getExternalEvents(accessToken, inactiveEventsEndpoint)
    }

    private suspend fun getExternalEvents(accessToken: AccessToken, completePathToEndpoint: URL): List<Beskjed> {
        return client.get(completePathToEndpoint, accessToken)
    }
}
