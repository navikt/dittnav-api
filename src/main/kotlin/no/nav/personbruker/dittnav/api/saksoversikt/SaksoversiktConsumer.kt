package no.nav.personbruker.dittnav.api.saksoversikt

import io.ktor.client.*
import no.nav.personbruker.dittnav.api.config.get
import no.nav.personbruker.dittnav.api.saksoversikt.external.PaabegynteSoknaderExternal
import no.nav.personbruker.dittnav.api.tokenx.AccessToken
import java.net.URL

class SaksoversiktConsumer(
    private val client: HttpClient,
    saksoversiktApiBaseURL: URL
) {

    private val paabegynteSoknaderEndpoint = URL("$saksoversiktApiBaseURL/tjenester/saker/paabegynte")

    suspend fun getPaabegynteSoknader(accessToken: AccessToken): PaabegynteSoknaderExternal {
        return client.get(paabegynteSoknaderEndpoint, accessToken)
    }
}
