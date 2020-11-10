package no.nav.personbruker.dittnav.api.varsel

import io.ktor.client.*
import no.nav.personbruker.dittnav.api.config.get
import no.nav.personbruker.dittnav.common.security.AuthenticatedUser
import java.net.URL

class VarselConsumer(
    private val client: HttpClient,
    private val legacyApiBaseURL: URL,
    private val pathToEndpoint: URL = URL("$legacyApiBaseURL/varselinnboks")
) {

    suspend fun getSisteVarsler(user: AuthenticatedUser): List<Varsel> {
        val completePathToEndpoint = URL("$pathToEndpoint/siste")
        return getSisteVarsler(user, completePathToEndpoint)
    }

    private suspend fun getSisteVarsler(user: AuthenticatedUser, completePathToEndpoint: URL): List<Varsel> {
        return client.get(completePathToEndpoint, user)
    }

}
