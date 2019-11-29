package no.nav.personbruker.dittnav.api.innboks

import no.nav.personbruker.dittnav.api.config.Environment
import no.nav.personbruker.dittnav.api.config.HttpClientBuilder
import no.nav.personbruker.dittnav.api.config.get

class InnboksConsumer(private val httpClientBuilder: HttpClientBuilder, private val environment: Environment) {

    suspend fun getExternalEvents(token: String): List<Innboks> {
        return httpClientBuilder.build().get("${environment.dittNAVEventsURL}/fetch/innboks", token)
    }
}