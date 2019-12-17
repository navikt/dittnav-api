package no.nav.personbruker.dittnav.api.innboks

import io.ktor.client.HttpClient
import no.nav.personbruker.dittnav.api.config.Environment
import no.nav.personbruker.dittnav.api.config.get

class InnboksConsumer(private val client: HttpClient, private val environment: Environment) {

    suspend fun getExternalEvents(token: String): List<Innboks> {
        return client.get("${environment.dittNAVEventsURL}/fetch/innboks", token)
    }
}
