package no.nav.personbruker.dittnav.api.digisos

import io.ktor.client.*
import no.nav.personbruker.dittnav.api.config.get
import no.nav.personbruker.dittnav.common.security.AuthenticatedUser
import java.net.URL

class DigiSosConsumer(
    private val client: HttpClient,
    digiSosBaseURL: URL,
) {

    private val paabegynteEndpoint = URL("$digiSosBaseURL/dittnav/pabegynte")

    suspend fun getPaabegynte(user: AuthenticatedUser): List<Paabegynte> {
        return client.get(paabegynteEndpoint, user)
    }

}
