package no.nav.personbruker.dittnav.api.saker

import io.ktor.client.HttpClient
import no.nav.personbruker.dittnav.api.config.getWithTokenx
import no.nav.personbruker.dittnav.api.tokenx.AccessToken
import java.net.URL

class MineSakerConsumer(
    private val client: HttpClient,
    mineSakerApiURL: URL
) {

    private val sisteEndredeSakerEndpoint = URL("$mineSakerApiURL/sakstemaer/sistendret")

    suspend fun hentSistEndret(user: AccessToken): SisteSakstemaerDTO {
        val external = client.getWithTokenx<SisteSakstemaer>(sisteEndredeSakerEndpoint, user)
        return external.toInternal()
    }

}
