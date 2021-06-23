package no.nav.personbruker.dittnav.api.brukernotifikasjon

import io.ktor.client.HttpClient
import no.nav.personbruker.dittnav.api.config.get
import no.nav.personbruker.dittnav.common.security.AuthenticatedUser
import java.net.URL

class BrukernotifikasjonConsumer(
        private val client: HttpClient,
        eventHandlerBaseURL: URL
) {

    private val countInactiveEndpoint = URL("$eventHandlerBaseURL/count/brukernotifikasjoner/inactive")
    private val countActiveEndpoint = URL("$eventHandlerBaseURL/count/brukernotifikasjoner/active")
    private val countEndpoint = URL("$eventHandlerBaseURL/count/brukernotifikasjoner")

    suspend fun countInactive(user: AuthenticatedUser): Int {
        return client.get(countInactiveEndpoint, user)
    }

    suspend fun countActive(user: AuthenticatedUser): Int {
        return client.get(countActiveEndpoint, user)
    }

    suspend fun count(user: AuthenticatedUser): Int {
        return client.get(countEndpoint, user)
    }

}
