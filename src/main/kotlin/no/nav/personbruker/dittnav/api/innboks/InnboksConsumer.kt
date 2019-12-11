package no.nav.personbruker.dittnav.api.innboks

import no.nav.personbruker.dittnav.api.config.HttpClientBuilder
import no.nav.personbruker.dittnav.api.config.get
import java.net.URL

class InnboksConsumer(
        private val httpClientBuilder: HttpClientBuilder,
        private val dittNAVEventsBaseURL: URL,
        private val completePathToEndpoint: URL = URL("$dittNAVEventsBaseURL/fetch/innboks")
) {

    suspend fun getExternalEvents(token: String): List<Innboks> {
        return httpClientBuilder.build().get(completePathToEndpoint.toString(), token)
    }
}