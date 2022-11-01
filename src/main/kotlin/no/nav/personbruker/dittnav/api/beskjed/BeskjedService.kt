package no.nav.personbruker.dittnav.api.beskjed

import mu.KotlinLogging
import no.nav.personbruker.dittnav.api.authentication.AuthenticatedUser
import no.nav.personbruker.dittnav.api.tokenx.EventhandlerTokendings
import no.nav.personbruker.dittnav.api.common.MultiSourceResult
import no.nav.personbruker.dittnav.api.tokenx.AccessToken

class BeskjedService(
    private val beskjedConsumer: BeskjedConsumer,
    private val eventhandlerTokendings: EventhandlerTokendings
) {

    private val log = KotlinLogging.logger {  }

    private val kilde = KildeType.EVENTHANDLER

    suspend fun getActiveBeskjedEvents(user: AuthenticatedUser): MultiSourceResult<BeskjedDTO, KildeType> {
        val exchangedToken = eventhandlerTokendings.exchangeToken(user)
        return getBeskjedEvents(user, exchangedToken) {
            beskjedConsumer.getExternalActiveEvents(exchangedToken)
        }
    }

    suspend fun getInactiveBeskjedEvents(user: AuthenticatedUser): MultiSourceResult<BeskjedDTO, KildeType> {
        val exchangedToken = eventhandlerTokendings.exchangeToken(user)
        return getBeskjedEvents(user, exchangedToken) {
            beskjedConsumer.getExternalInactiveEvents(exchangedToken)
        }
    }

    private suspend fun getBeskjedEvents(
        user: AuthenticatedUser,
        exchangedToken: AccessToken,
        getEvents: suspend (AccessToken) -> List<Beskjed>
    ): MultiSourceResult<BeskjedDTO, KildeType> {
        return try {
            val externalEvents = getEvents(exchangedToken)
            val results = externalEvents.map { beskjed -> transformToDTO(beskjed, user.loginLevel) }
            MultiSourceResult.createSuccessfulResult(results, kilde)

        } catch (e: Exception) {
            log.warn("Klarte ikke å hente data fra $kilde: $e", e)
            MultiSourceResult.createErrorResult(kilde)
        }
    }

    private fun transformToDTO(beskjed: Beskjed, operatingLoginLevel: Int): BeskjedDTO {
        return if (userIsAllowedToViewAllDataInEvent(beskjed, operatingLoginLevel)) {
            toBeskjedDTO(beskjed)
        } else {
            toMaskedBeskjedDTO(beskjed)
        }
    }

    private fun userIsAllowedToViewAllDataInEvent(beskjed: Beskjed, operatingLoginLevel: Int): Boolean {
        return operatingLoginLevel >= beskjed.sikkerhetsnivaa
    }

    private fun getHighestRequiredLoginLevel(beskjedList: List<Beskjed>): Int {
        return if (beskjedList.isEmpty()) {
            0
        } else {
            beskjedList.maxOf { it.sikkerhetsnivaa }
        }
    }
}
