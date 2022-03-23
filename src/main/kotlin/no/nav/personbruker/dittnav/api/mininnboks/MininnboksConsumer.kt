package no.nav.personbruker.dittnav.api.mininnboks

import io.ktor.client.*
import no.nav.personbruker.dittnav.api.config.get
import no.nav.personbruker.dittnav.api.mininnboks.external.UbehandletMeldingExternal
import no.nav.personbruker.dittnav.api.tokenx.AccessToken
import java.net.URL

class MininnboksConsumer(
    private val client: HttpClient,
    mininnboksBaseURL: URL,
) {

    private val ubehandledeMeldingerEndpoint = URL("$mininnboksBaseURL/sporsmal/ubehandlet")

    suspend fun getUbehandledeMeldinger(accessToken: AccessToken): List<UbehandletMeldingExternal> {
        return client.get(ubehandledeMeldingerEndpoint, accessToken)
    }
}
