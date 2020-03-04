package no.nav.personbruker.dittnav.api.innboks

import io.ktor.client.HttpClient
import no.nav.personbruker.dittnav.api.common.InnloggetBruker
import no.nav.personbruker.dittnav.api.config.get
import java.net.URL

class InnboksConsumer(
        private val client: HttpClient,
        private val eventHandlerBaseURL: URL,
        private val completePathToEndpoint: URL = URL("$eventHandlerBaseURL/fetch/innboks")
) {

    suspend fun getExternalEvents(innloggetBruker: InnloggetBruker): List<Innboks> {
        return client.get(completePathToEndpoint, innloggetBruker)
    }
}
