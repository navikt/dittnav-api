package no.nav.personbruker.dittnav.api.meldekort

import io.ktor.client.HttpClient
import no.nav.personbruker.dittnav.api.config.getWithMeldekortTokenx
import no.nav.personbruker.dittnav.api.tokenx.AccessToken
import java.net.URL

class MeldekortConsumer(
    private val client: HttpClient,
    meldekortApiBaseURL: URL,
) {

    private val meldekortStatusEndpoint = URL("$meldekortApiBaseURL/api/person/meldekortstatus")

    suspend fun getMeldekortStatus(accessToken: AccessToken): MeldekortstatusExternal {
        return client.getWithMeldekortTokenx(meldekortStatusEndpoint, accessToken)
    }
}
