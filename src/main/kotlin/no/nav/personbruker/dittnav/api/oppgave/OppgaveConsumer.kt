package no.nav.personbruker.dittnav.api.oppgave

import no.nav.personbruker.dittnav.api.config.Environment
import no.nav.personbruker.dittnav.api.config.HttpClientBuilder
import no.nav.personbruker.dittnav.api.config.get

class OppgaveConsumer(private val httpClientBuilder: HttpClientBuilder, private val environment: Environment) {

    suspend fun getExternalEvents(token: String): List<Oppgave> {
        return httpClientBuilder.build().get("${environment.dittNAVEventsURL}/fetch/oppgave", token)
    }
}
