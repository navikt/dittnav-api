package no.nav.personbruker.dittnav.api.innboks

import no.nav.personbruker.dittnav.api.common.ConsumeEventException
import no.nav.personbruker.dittnav.api.loginstatus.LoginLevelService
import no.nav.personbruker.dittnav.api.tokenx.AccessToken
import no.nav.personbruker.dittnav.api.tokenx.EventhandlerTokendings
import no.nav.personbruker.dittnav.common.security.AuthenticatedUser

class InnboksService (
    private val innboksConsumer: InnboksConsumer,
    private val eventhandlerTokenDings: EventhandlerTokendings,
    private val loginLevelService: LoginLevelService) {

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
            val highestRequiredLoginLevel = getHighestRequiredLoginLevel(externalEvents)
            val operatingLoginLevel = loginLevelService.getOperatingLoginLevel(user, highestRequiredLoginLevel)
            externalEvents.map { innboks -> transformToDTO(innboks, operatingLoginLevel) }
        } catch (exception: Exception) {
            throw ConsumeEventException("Klarte ikke hente eventer av type Innboks", exception)
        }
    }

    private fun transformToDTO(innboks: Innboks, operatingLoginLevel: Int): InnboksDTO {
        return if(userIsAllowedToViewAllDataInEvent(innboks, operatingLoginLevel)) {
            toInnboksDTO(innboks)
        } else {
            toMaskedInnboksDTO(innboks)
        }
    }

    private fun userIsAllowedToViewAllDataInEvent(innboks: Innboks, operatingLoginLevel: Int): Boolean {
        return operatingLoginLevel >= innboks.sikkerhetsnivaa
    }

    private fun getHighestRequiredLoginLevel(innboksList: List<Innboks>): Int {
        return if (innboksList.isEmpty()) {
            0
        } else {
            innboksList.maxOf { it.sikkerhetsnivaa }
        }
    }
}
