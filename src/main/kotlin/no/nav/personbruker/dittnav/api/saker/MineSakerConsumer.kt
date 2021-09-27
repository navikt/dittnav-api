package no.nav.personbruker.dittnav.api.saker

import io.ktor.client.*
import no.nav.personbruker.dittnav.api.config.get
import no.nav.personbruker.dittnav.common.security.AuthenticatedUser
import java.net.URL

class MineSakerConsumer(
        private val client: HttpClient,
        sakerApiURL: URL
) {

    private val sisteEndredeSakerEndpoint = URL("$sakerApiURL/sakstemaer/sistendret")

    suspend fun getExternalSaker(user: AuthenticatedUser): List<Sakstema> {
        return client.get(sisteEndredeSakerEndpoint, user)
    }

}
