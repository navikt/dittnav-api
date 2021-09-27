package no.nav.personbruker.dittnav.api.saker

import io.ktor.client.*
import no.nav.personbruker.dittnav.api.config.get
import no.nav.personbruker.dittnav.api.saker.legacy.LegacySaker
import no.nav.personbruker.dittnav.common.security.AuthenticatedUser
import java.net.URL

class SakerClient(
        private val client: HttpClient,
        sakerApiURL: URL
) {

    private val sisteEndredeSakerEndpoint = URL("$sakerApiURL/sakstemaer/sistendret")

    suspend fun getExternalLegacySaker(user: AuthenticatedUser): LegacySaker {
        return client.get(sisteEndredeSakerEndpoint, user)
    }

    suspend fun getExternalSaker(user: AuthenticatedUser): List<Saker> {
        return client.get(sisteEndredeSakerEndpoint, user)
    }

}

