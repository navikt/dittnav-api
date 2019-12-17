package no.nav.personbruker.dittnav.api.beskjed

import io.ktor.client.HttpClient
import no.nav.personbruker.dittnav.api.config.get
import java.net.URL

class BeskjedConsumer(
        private val client: HttpClient,
        private val dittNAVEventsBaseURL: URL,
        private val completePathToEndpoint: URL = URL("$dittNAVEventsBaseURL/fetch/beskjed")
) {

    suspend fun getExternalEvents(token: String): List<Beskjed> {
        return client.get(completePathToEndpoint, token)
    }
}
