package no.nav.personbruker.dittnav.api.oppgave

import no.nav.personbruker.dittnav.api.config.HttpClientBuilder
import no.nav.personbruker.dittnav.api.config.get
import java.net.URL

class OppgaveConsumer(
        private val httpClientBuilder: HttpClientBuilder,
        private val dittNAVEventsBaseURL: URL,
        private val completePathToEndpoint: URL = URL("$dittNAVEventsBaseURL/fetch/oppgave")
) {

    suspend fun getExternalEvents(token: String): List<Oppgave> {
        return httpClientBuilder.build().get(completePathToEndpoint.toString(), token)
    }
}
