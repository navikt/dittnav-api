package no.nav.personbruker.dittnav.api.oppgave

import io.ktor.client.HttpClient
import no.nav.personbruker.dittnav.api.config.Environment
import no.nav.personbruker.dittnav.api.config.get

class OppgaveConsumer(private val client: HttpClient, private val environment: Environment) {

    suspend fun getExternalEvents(token: String): List<Oppgave> {
        return client.get("${environment.dittNAVEventsURL}/fetch/oppgave", token)
    }
}
