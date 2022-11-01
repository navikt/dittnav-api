package no.nav.personbruker.dittnav.api.beskjed


import io.ktor.client.HttpClient
import mu.KotlinLogging
import no.nav.personbruker.dittnav.api.authentication.AuthenticatedUser
import no.nav.personbruker.dittnav.api.common.MultiSourceResult

import no.nav.personbruker.dittnav.api.config.get
import no.nav.personbruker.dittnav.api.tokenx.EventhandlerTokendings
import java.net.URL

class BeskjedConsumer(
    private val client: HttpClient,
    private val eventhandlerTokendings: EventhandlerTokendings,
    eventHandlerBaseURL: URL
) {

    private val activeEventsEndpoint = URL("$eventHandlerBaseURL/fetch/beskjed/aktive")
    private val inactiveEventsEndpoint = URL("$eventHandlerBaseURL/fetch/beskjed/inaktive")
    private val kilde = KildeType.EVENTHANDLER
    private val log = KotlinLogging.logger { }

    suspend fun getActiveBeskjedEvents(user: AuthenticatedUser): MultiSourceResult<BeskjedDTO, KildeType> {
        val exchangedToken = eventhandlerTokendings.exchangeToken(user)
        return getBeskjedEvents(user, exchangedToken, activeEventsEndpoint)
    }

    suspend fun getInactiveBeskjedEvents(user: AuthenticatedUser): MultiSourceResult<BeskjedDTO, KildeType> {
        val exchangedToken = eventhandlerTokendings.exchangeToken(user)
        return getBeskjedEvents(user, exchangedToken, inactiveEventsEndpoint)
    }

    private suspend fun getBeskjedEvents(
        user: AuthenticatedUser,
        exchangedToken: String,
        completePathToEndpoint: URL,
    ): MultiSourceResult<BeskjedDTO, KildeType> =
        try {
            client.get<List<Beskjed>>(completePathToEndpoint, exchangedToken).let { externalEvents ->
                MultiSourceResult.createSuccessfulResult(
                    results = externalEvents.map { beskjed -> beskjed.toBeskjedDto(user.loginLevel) },
                    source = kilde
                )
            }

        } catch (e: Exception) {
            log.warn("Klarte ikke å hente data fra $kilde: $e", e)
            MultiSourceResult.createErrorResult(kilde)
        }
}

