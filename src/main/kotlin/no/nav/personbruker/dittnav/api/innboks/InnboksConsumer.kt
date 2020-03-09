package no.nav.personbruker.dittnav.api.innboks

import io.ktor.client.HttpClient
import no.nav.personbruker.dittnav.api.common.InnloggetBruker
import no.nav.personbruker.dittnav.api.config.get
import java.net.URL

class InnboksConsumer(
        private val client: HttpClient,
        private val eventHandlerBaseURL: URL,
        private val pathToEndpoint: URL = URL("$eventHandlerBaseURL/fetch/innboks")
) {

    suspend fun getExternalActiveEvents(innloggetBruker: InnloggetBruker): List<Innboks> {
        val completePathToEndpoint = URL("$pathToEndpoint/aktive")
        val externalActiveEvents = getExternalEvents(innloggetBruker, completePathToEndpoint)
        return externalActiveEvents
    }

    suspend fun getExternalInactiveEvents(innloggetBruker: InnloggetBruker): List<Innboks> {
        val completePathToEndpoint = URL("$pathToEndpoint/inaktive")
        val externalInactiveEvents = getExternalEvents(innloggetBruker, completePathToEndpoint)
        return externalInactiveEvents
    }

    private suspend fun getExternalEvents(innloggetBruker: InnloggetBruker, completePathToEndpoint: URL): List<Innboks> {
        return client.get(completePathToEndpoint, innloggetBruker)
    }
}
