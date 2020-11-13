package no.nav.personbruker.dittnav.api.beskjed

import no.nav.personbruker.dittnav.api.loginstatus.LoginLevelService
import no.nav.personbruker.dittnav.common.security.AuthenticatedUser

class BeskjedService(private val beskjedConsumer: BeskjedConsumer, private val loginLevelService: LoginLevelService) {

    suspend fun getActiveBeskjedEvents(user: AuthenticatedUser): BeskjedResult {
        return getBeskjedEvents(user) {
            beskjedConsumer.getExternalActiveEvents(it)
        }
    }

    suspend fun getInactiveBeskjedEvents(user: AuthenticatedUser): BeskjedResult {
        return getBeskjedEvents(user) {
            beskjedConsumer.getExternalInactiveEvents(it)
        }
    }

    private suspend fun getBeskjedEvents(
            user: AuthenticatedUser,
            getEvents: suspend (AuthenticatedUser) -> List<Beskjed>
    ): BeskjedResult {
        return try {
            val externalEvents = getEvents(user)
            val highestRequiredLoginLevel = getHighestRequiredLoginLevel(externalEvents)
            val operatingLoginLevel = loginLevelService.getOperatingLoginLevel(user, highestRequiredLoginLevel)
            val results = externalEvents.map { beskjed -> transformToDTO(beskjed, operatingLoginLevel) }
            BeskjedResult(results)
        } catch(exception: Exception) {
            BeskjedResult(listOf(KildeType.EVENTHANDLER))
        }
    }

    private fun transformToDTO(beskjed: Beskjed, operatingLoginLevel: Int): BeskjedDTO {
        return if(userIsAllowedToViewAllDataInEvent(beskjed, operatingLoginLevel)) {
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
