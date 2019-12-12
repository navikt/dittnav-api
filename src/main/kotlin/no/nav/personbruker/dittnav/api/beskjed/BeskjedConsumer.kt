package no.nav.personbruker.dittnav.api.beskjed

import no.nav.personbruker.dittnav.api.config.HttpClientBuilder
import no.nav.personbruker.dittnav.api.config.get
import java.net.URL

class BeskjedConsumer(
        private val httpClientBuilder: HttpClientBuilder,
        private val dittNAVEventsBaseURL: URL,
        private val completePathToEndpoint: URL = URL("$dittNAVEventsBaseURL/fetch/beskjed")
) {

    suspend fun getExternalEvents(token: String): List<Beskjed> {
        return httpClientBuilder.build().get(completePathToEndpoint.toString(), token)
    }
}
