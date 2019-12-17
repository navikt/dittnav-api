package no.nav.personbruker.dittnav.api.beskjed

import io.ktor.client.HttpClient
import no.nav.personbruker.dittnav.api.config.Environment
import no.nav.personbruker.dittnav.api.config.get

class BeskjedConsumer(private val client: HttpClient, private val environment: Environment) {

    suspend fun getExternalEvents(token: String): List<Beskjed> {
        return client.get("${environment.dittNAVEventsURL}/fetch/beskjed", token)
    }
}
