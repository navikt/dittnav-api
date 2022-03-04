package no.nav.personbruker.dittnav.api.health

import io.ktor.client.*
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import no.nav.personbruker.dittnav.api.config.Environment
import java.net.URL

class DependencyPinger(
    environment: Environment,
    private val client: HttpClient
) {

    private val legacyApiPingableURL = URL("${environment.legacyApiURL}/internal/isAlive")

    suspend fun pingAll() = coroutineScope {
        val legacySelftestStatus = async {
            getStatus(legacyApiPingableURL, client)
        }

        val services = mutableMapOf("DITTNAV_LEGACY_API:" to legacySelftestStatus.await())
        return@coroutineScope services
    }
}
