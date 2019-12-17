package no.nav.personbruker.dittnav.api.oppgave

import io.ktor.client.HttpClient
import no.nav.personbruker.dittnav.api.config.get
import java.net.URL

class OppgaveConsumer(
    private val client: HttpClient,
    private val dittNAVEventsBaseURL: URL,
    private val completePathToEndpoint: URL = URL("$dittNAVEventsBaseURL/fetch/oppgave")
) {

    suspend fun getExternalEvents(token: String): List<Oppgave> {
        return client.get(completePathToEndpoint, token)
    }
}
