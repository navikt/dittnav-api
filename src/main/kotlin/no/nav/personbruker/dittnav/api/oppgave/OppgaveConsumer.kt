package no.nav.personbruker.dittnav.api.oppgave

import io.ktor.client.HttpClient
import no.nav.personbruker.dittnav.api.authentication.AuthenticatedUser
import no.nav.personbruker.dittnav.api.config.ConsumeEventException
import no.nav.personbruker.dittnav.api.config.get
import no.nav.personbruker.dittnav.api.tokenx.EventhandlerTokendings
import java.net.URL

class OppgaveConsumer(
    private val client: HttpClient,
    private val eventhandlerTokendings: EventhandlerTokendings,
    eventHandlerBaseURL: URL,
) {

    private val inactiveEventsEndpoint = URL("$eventHandlerBaseURL/fetch/oppgave/inaktive")
    private val activeEventsEndpoint = URL("$eventHandlerBaseURL/fetch/oppgave/aktive")

    suspend fun getActiveOppgaver(user: AuthenticatedUser): List<OppgaveDTO> {
        val exchangedToken = eventhandlerTokendings.exchangeToken(user)
        return getOppgaveEvents(user, exchangedToken, activeEventsEndpoint)
    }

    suspend fun getInactiveOppgaver(user: AuthenticatedUser): List<OppgaveDTO> {
        val exchangedToken = eventhandlerTokendings.exchangeToken(user)
        return getOppgaveEvents(user, exchangedToken, inactiveEventsEndpoint)
    }

    private suspend fun getOppgaveEvents(
        user: AuthenticatedUser,
        exchangedToken: String,
        url: URL
    ): List<OppgaveDTO> =
        try {
            client.get<List<Oppgave>>(url, exchangedToken).map { oppgave ->
                oppgave.toOppgaveDTO(user.loginLevel)
            }
        } catch (e: Exception) {
            throw ConsumeEventException("Klarte ikke hente oppgaver", e)
        }

}
