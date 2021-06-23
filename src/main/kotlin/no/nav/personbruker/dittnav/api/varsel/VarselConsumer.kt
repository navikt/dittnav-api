package no.nav.personbruker.dittnav.api.varsel

import io.ktor.client.*
import no.nav.personbruker.dittnav.api.config.get
import no.nav.personbruker.dittnav.common.security.AuthenticatedUser
import java.net.URL

class VarselConsumer(
    private val client: HttpClient,
    legacyApiBaseURL: URL,
) {

    private val endpoint = URL("$legacyApiBaseURL/varselinnboks/siste")

    suspend fun getSisteVarsler(user: AuthenticatedUser): List<Varsel> {
        return getSisteVarsler(user, endpoint)
    }

    private suspend fun getSisteVarsler(user: AuthenticatedUser, completePathToEndpoint: URL): List<Varsel> {
        return client.get(completePathToEndpoint, user)
    }

}
