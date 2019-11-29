package no.nav.personbruker.dittnav.api.beskjed

import no.nav.personbruker.dittnav.api.config.Environment
import no.nav.personbruker.dittnav.api.config.HttpClientBuilder
import no.nav.personbruker.dittnav.api.config.get

class BeskjedConsumer(private val httpClientBuilder: HttpClientBuilder, private val environment: Environment) {

    suspend fun getExternalEvents(token: String): List<Beskjed> {
        return httpClientBuilder.build().get("${environment.dittNAVEventsURL}/fetch/beskjed", token)
    }
}
