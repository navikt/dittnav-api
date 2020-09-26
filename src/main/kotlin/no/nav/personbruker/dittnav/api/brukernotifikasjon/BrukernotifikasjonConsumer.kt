package no.nav.personbruker.dittnav.api.brukernotifikasjon

import io.ktor.client.HttpClient
import no.nav.personbruker.dittnav.api.common.InnloggetBruker
import no.nav.personbruker.dittnav.api.config.get
import java.net.URL

class BrukernotifikasjonConsumer(
        private val client: HttpClient,
        private val eventHandlerBaseURL: URL,
        private val pathToEndpoint: URL = URL("$eventHandlerBaseURL/count/brukernotifikasjoner")
) {

    suspend fun countActive(innloggetBruker: InnloggetBruker): Int {
        val completePathToEndpoint = URL("$pathToEndpoint/active")
        return client.get(completePathToEndpoint, innloggetBruker)
    }

    suspend fun count(innloggetBruker: InnloggetBruker): Int {
        val completePathToEndpoint = URL("$pathToEndpoint")
        return client.get(completePathToEndpoint, innloggetBruker)
    }

}
