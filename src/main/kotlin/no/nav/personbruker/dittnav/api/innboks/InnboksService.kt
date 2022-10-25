package no.nav.personbruker.dittnav.api.innboks

import no.nav.personbruker.dittnav.api.authentication.AuthenticatedUser
import no.nav.personbruker.dittnav.api.config.ConsumeEventException
import no.nav.personbruker.dittnav.api.tokenx.AccessToken
import no.nav.personbruker.dittnav.api.tokenx.EventhandlerTokendings


class InnboksService (
    private val innboksConsumer: InnboksConsumer,
    private val eventhandlerTokenDings: EventhandlerTokendings
) {

    suspend fun getActiveInnboksEvents(user: AuthenticatedUser): List<InnboksDTO> {
        val exchangedToken = eventhandlerTokenDings.exchangeToken(user)
        return getInnboksEvents(user, exchangedToken) {
            innboksConsumer.getExternalActiveEvents(exchangedToken)
        }
    }

    suspend fun getInactiveInnboksEvents(user: AuthenticatedUser): List<InnboksDTO> {
        val exchangedToken = eventhandlerTokenDings.exchangeToken(user)
        return getInnboksEvents(user, exchangedToken) {
            innboksConsumer.getExternalInactiveEvents(exchangedToken)
        }
    }

    private suspend fun getInnboksEvents(
            user: AuthenticatedUser,
            exchangedToken: AccessToken,
            getEvents: suspend (AccessToken) -> List<Innboks>
    ): List<InnboksDTO> {
        return try {
            val externalEvents = getEvents(exchangedToken)
            externalEvents.map { innboks -> innboks.toInnboksDTO(user.loginLevel) }
        } catch (exception: Exception) {
            throw ConsumeEventException("Klarte ikke hente eventer av type Innboks", exception)
        }
    }
}
