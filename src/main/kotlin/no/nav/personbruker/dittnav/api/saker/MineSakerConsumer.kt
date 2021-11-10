package no.nav.personbruker.dittnav.api.saker

import io.ktor.client.*
import no.nav.personbruker.dittnav.api.config.getExtendedTimeout
import no.nav.personbruker.dittnav.api.saker.ekstern.SisteSakstemaer
import no.nav.personbruker.dittnav.api.tokenx.AccessToken
import java.net.URL

class MineSakerConsumer(
    private val client: HttpClient,
    mineSakerApiURL: URL
) {

    private val sisteEndredeSakerEndpoint = URL("$mineSakerApiURL/sakstemaer/sistendret")

    suspend fun hentSistEndret(user: AccessToken): SisteSakstemaerDTO {
        val external = client.getExtendedTimeout<SisteSakstemaer>(sisteEndredeSakerEndpoint, user)
        return external.toInternal()
    }

}
