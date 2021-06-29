package no.nav.personbruker.dittnav.api.digisos

import io.ktor.client.*
import no.nav.personbruker.dittnav.api.config.get
import no.nav.personbruker.dittnav.common.security.AuthenticatedUser
import java.net.URL

class DigiSosConsumer(
    private val client: HttpClient,
    digiSosBaseURL: URL,
) {

    private val aktivePaabegynteEndpoint = URL("$digiSosBaseURL/dittnav/pabegynte")
    private val inaktivePaabegynteEndpoint = URL("$digiSosBaseURL/dittnav/pabegynte/inaktive")

    suspend fun getPaabegynteActive(user: AuthenticatedUser): List<Paabegynte> {
        return client.get(aktivePaabegynteEndpoint, user)
    }

    suspend fun getPaabegynteInactive(user: AuthenticatedUser): List<Paabegynte> {
        return client.get(inaktivePaabegynteEndpoint, user)
    }

}
