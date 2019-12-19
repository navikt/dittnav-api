package no.nav.personbruker.dittnav.api.innboks

import io.ktor.client.HttpClient
import no.nav.personbruker.dittnav.api.config.get
import java.net.URL

class InnboksConsumer(
        private val client: HttpClient,
        private val dittNAVEventsBaseURL: URL,
        private val completePathToEndpoint: URL = URL("$dittNAVEventsBaseURL/fetch/innboks")
) {

    suspend fun getExternalEvents(token: String): List<Innboks> {
        return client.get(completePathToEndpoint, token)
    }
}
