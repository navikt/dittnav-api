package no.nav.personbruker.dittnav.api.innboks


import io.ktor.client.HttpClient
import no.nav.personbruker.dittnav.api.authentication.AuthenticatedUser
import no.nav.personbruker.dittnav.api.config.ConsumeEventException
import no.nav.personbruker.dittnav.api.config.get
import no.nav.personbruker.dittnav.api.tokenx.EventhandlerTokendings
import java.net.URL

class InnboksConsumer(
    private val client: HttpClient,
    private val eventhandlerTokenDings: EventhandlerTokendings,
    eventHandlerBaseURL: URL
) {

    private val activeEventsEndpoint = URL("$eventHandlerBaseURL/fetch/innboks/aktive")
    private val inactiveEventsEndpoint = URL("$eventHandlerBaseURL/fetch/innboks/inaktive")


    suspend fun getActiveInnboksEvents(user: AuthenticatedUser): List<InnboksDTO> {
        val exchangedToken = eventhandlerTokenDings.exchangeToken(user)
        return getInnboksEvents(user, exchangedToken, activeEventsEndpoint)
    }

    suspend fun getInactiveInnboksEvents(user: AuthenticatedUser): List<InnboksDTO> {
        val exchangedToken = eventhandlerTokenDings.exchangeToken(user)
        return getInnboksEvents(user, exchangedToken, inactiveEventsEndpoint)
    }

    private suspend fun getInnboksEvents(
        user: AuthenticatedUser,
        exchangedToken: String,
        completePathToEndpoint: URL
    ): List<InnboksDTO> =
        try {
            client.get<List<Innboks>>(completePathToEndpoint, exchangedToken).map { innboks ->
                innboks.toInnboksDTO(user.loginLevel)
            }
        } catch (exception: Exception) {
            throw ConsumeEventException("Klarte ikke hente eventer av type Innboks", exception)
        }

}
