package no.nav.personbruker.dittnav.api.oppgave

import io.ktor.client.HttpClient
import no.nav.personbruker.dittnav.api.common.InnloggetBruker
import no.nav.personbruker.dittnav.api.config.get
import java.net.URL

class OppgaveConsumer(
        private val client: HttpClient,
        private val eventHandlerBaseURL: URL,
        private val completePathToEndpoint: URL = URL("$eventHandlerBaseURL/fetch/oppgave")
) {

    suspend fun getExternalEvents(innloggetBruker: InnloggetBruker): List<Oppgave> {
        return client.get(completePathToEndpoint, innloggetBruker)
    }
}
