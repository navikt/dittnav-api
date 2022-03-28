package no.nav.personbruker.dittnav.api.meldekort

import io.ktor.client.*
import no.nav.personbruker.dittnav.api.config.get
import no.nav.personbruker.dittnav.api.config.getWithMeldekortTokenx
import no.nav.personbruker.dittnav.api.meldekort.external.MeldekortstatusExternal
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
