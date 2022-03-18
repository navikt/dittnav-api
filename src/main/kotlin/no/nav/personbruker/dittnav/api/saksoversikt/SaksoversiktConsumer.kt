package no.nav.personbruker.dittnav.api.saksoversikt

import io.ktor.client.*
import no.nav.personbruker.dittnav.api.config.get
import no.nav.personbruker.dittnav.api.saksoversikt.external.PaabegynteSoknaderExternal
import no.nav.personbruker.dittnav.api.saksoversikt.external.SakstemaExternal
import no.nav.personbruker.dittnav.api.tokenx.AccessToken
import java.net.URL

class SaksoversiktConsumer(
    private val client: HttpClient,
    saksoversiktApiBaseURL: URL,
) {

    private val paabegynteSoknaderEndpoint = URL("$saksoversiktApiBaseURL/tjenester/saker/paabegynte")
    private val sakstemaEndpoint = URL("$saksoversiktApiBaseURL/tjenester/sakstema/hentForenkletSakstema")

    suspend fun getPaabegynteSoknader(accessToken: AccessToken): PaabegynteSoknaderExternal {
        return client.get(paabegynteSoknaderEndpoint, accessToken)
    }

    suspend fun getSakstema(accessToken: AccessToken): List<SakstemaExternal> {
        return client.get(sakstemaEndpoint, accessToken)
    }
}
