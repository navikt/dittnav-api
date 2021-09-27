package no.nav.personbruker.dittnav.api.saker

import io.ktor.client.*
import no.nav.personbruker.dittnav.api.config.get
import no.nav.personbruker.dittnav.common.security.AuthenticatedUser
import java.net.URL

class MineSakerConsumer(
    private val client: HttpClient,
    mineSakerApiURL: URL
) {

    private val sisteEndredeSakerEndpoint = URL("$mineSakerApiURL/sakstemaer/sistendret")

    suspend fun hentSistEndret(user: AuthenticatedUser): List<SakstemaDTO> {
        val externals = client.get<List<Sakstema>>(sisteEndredeSakerEndpoint, user)
        val internal = externals.map { external ->
            external.toInternal()
        }.toList()
        return internal
    }

}
