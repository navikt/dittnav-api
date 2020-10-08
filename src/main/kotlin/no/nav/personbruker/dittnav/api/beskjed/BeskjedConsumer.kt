package no.nav.personbruker.dittnav.api.beskjed

import io.ktor.client.*
import no.nav.personbruker.dittnav.api.common.InnloggetBruker
import no.nav.personbruker.dittnav.api.config.get
import java.net.URL

class BeskjedConsumer(
        private val client: HttpClient,
        private val eventHandlerBaseURL: URL,
        private val pathToEndpoint: URL = URL("$eventHandlerBaseURL/fetch/beskjed")
) {

    suspend fun getExternalActiveEvents(innloggetBruker: InnloggetBruker): List<Beskjed> {
        val completePathToEndpoint = URL("$pathToEndpoint/aktive")
        val externalActiveEvents = getExternalEvents(innloggetBruker, completePathToEndpoint)
        return externalActiveEvents
    }

    suspend fun getExternalInactiveEvents(innloggetBruker: InnloggetBruker): List<Beskjed> {
        val completePathToEndpoint = URL("$pathToEndpoint/inaktive")
        val externalInactiveEvents = getExternalEvents(innloggetBruker, completePathToEndpoint)
        return externalInactiveEvents
    }

    private suspend fun getExternalEvents(innloggetBruker: InnloggetBruker, completePathToEndpoint: URL): List<Beskjed> {
        return client.get(completePathToEndpoint, innloggetBruker)
    }
}
