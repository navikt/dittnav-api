package no.nav.personbruker.dittnav.api.varsel

import io.ktor.client.*
import no.nav.personbruker.dittnav.api.common.InnloggetBruker
import no.nav.personbruker.dittnav.api.config.get
import java.net.URL

class VarselConsumer(
    private val client: HttpClient,
    private val legacyApiBaseURL: URL,
    private val pathToEndpoint: URL = URL("$legacyApiBaseURL/varselinnboks")
) {

    suspend fun getSisteVarsler(innloggetBruker: InnloggetBruker): List<Varsel> {
        val completePathToEndpoint = URL("$pathToEndpoint/siste")
        return getSisteVarsler(innloggetBruker, completePathToEndpoint)
    }

    private suspend fun getSisteVarsler(innloggetBruker: InnloggetBruker, completePathToEndpoint: URL): List<Varsel> {
        return client.get(completePathToEndpoint, innloggetBruker)
    }

}
