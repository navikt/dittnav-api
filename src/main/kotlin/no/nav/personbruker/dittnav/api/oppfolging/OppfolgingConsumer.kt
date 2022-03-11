package no.nav.personbruker.dittnav.api.oppfolging

import io.ktor.client.*
import no.nav.personbruker.dittnav.api.config.get
import no.nav.personbruker.dittnav.api.oppfolging.external.OppfolgingExternal
import no.nav.personbruker.dittnav.api.tokenx.AccessToken
import java.net.URL

class OppfolgingConsumer(
    private val client: HttpClient,
    oppfolgingApiBaseURL: URL,
) {

    private val oppfolgingStatusEndpoint = URL("$oppfolgingApiBaseURL/api/niva3/underoppfolging")

    suspend fun getOppfolgingStatus(accessToken: AccessToken): OppfolgingExternal {
        return client.get(oppfolgingStatusEndpoint, accessToken)
    }
}
