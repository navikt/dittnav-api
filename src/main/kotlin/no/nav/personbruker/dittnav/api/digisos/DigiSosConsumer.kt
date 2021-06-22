package no.nav.personbruker.dittnav.api.digisos

import io.ktor.client.*
import no.nav.personbruker.dittnav.api.config.get
import no.nav.personbruker.dittnav.common.security.AuthenticatedUser
import java.net.URL

class DigiSosConsumer(
    private val client: HttpClient,
    private val digiSosBaseURL: URL,
    pathToEndpoint: URL = URL("$digiSosBaseURL/dittnav")
) {

    private val paabegynteEndpoint = URL("$pathToEndpoint/pabegynte")

    suspend fun getPaabegynte(user: AuthenticatedUser): List<Paabegynte> {
        return client.get(paabegynteEndpoint, user)
    }

}
