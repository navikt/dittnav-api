package no.nav.personbruker.dittnav.api.beskjed

import io.ktor.client.HttpClient
import no.nav.personbruker.dittnav.api.common.InnloggetBruker
import no.nav.personbruker.dittnav.api.config.get
import org.slf4j.LoggerFactory
import java.net.URL

class BeskjedConsumer(
        private val client: HttpClient,
        private val eventHandlerBaseURL: URL,
        private val pathToEndpoint: URL = URL("$eventHandlerBaseURL/fetch/beskjed")
) {

    private val log = LoggerFactory.getLogger(BeskjedConsumer::class.java)

    suspend fun getExternalActiveEvents(innloggetBruker: InnloggetBruker): List<Beskjed> {
        val completePathToEndpoint = URL("$pathToEndpoint/aktive")
        val externalActiveEvents = getExternalEvents(innloggetBruker, completePathToEndpoint)
        logWhenTokenIsAboutToExpire(innloggetBruker)
        return externalActiveEvents
    }

    suspend fun getExternalInactiveEvents(innloggetBruker: InnloggetBruker): List<Beskjed> {
        val completePathToEndpoint = URL("$pathToEndpoint/inaktive")
        val externalInactiveEvents = getExternalEvents(innloggetBruker, completePathToEndpoint)
        return externalInactiveEvents
    }

    private fun logWhenTokenIsAboutToExpire(innloggetBruker: InnloggetBruker) {
        val expiryThresholdInMinutes = 2L

        if (innloggetBruker.isTokenAboutToExpire(expiryThresholdInMinutes)) {
            log.info("Det er mindre enn $expiryThresholdInMinutes minutter før token-et går ut for: $innloggetBruker")
        }
    }

    private suspend fun getExternalEvents(innloggetBruker: InnloggetBruker, completePathToEndpoint: URL): List<Beskjed> {
        return client.get(completePathToEndpoint, innloggetBruker)
    }
}
