package no.nav.personbruker.dittnav.api.done

import io.ktor.client.*
import io.ktor.client.statement.*
import io.ktor.http.*
import mu.KotlinLogging
import no.nav.personbruker.dittnav.api.authentication.AuthenticatedUser
import no.nav.personbruker.dittnav.api.config.post
import no.nav.personbruker.dittnav.api.tokenx.EventhandlerTokendings

import java.net.URL

class DoneProducer(
    private val httpClient: HttpClient,
    private val eventhandlerTokendings: EventhandlerTokendings,
    dittNAVBaseURL: URL) {

    private val log = KotlinLogging.logger{}
    private val completePathToEndpoint = URL("$dittNAVBaseURL/produce/done")

    suspend fun postDoneEvents(done: DoneDTO, user: AuthenticatedUser): HttpResponse {
        val exchangedToken = eventhandlerTokendings.exchangeToken(user)
        val response: HttpResponse = httpClient.post(completePathToEndpoint, done, exchangedToken)
        if (response.status != HttpStatusCode.OK) {
            log.warn("Feil mot $completePathToEndpoint: ${response.status.value} ${response.status.description}")
        }
        return response
    }
}
