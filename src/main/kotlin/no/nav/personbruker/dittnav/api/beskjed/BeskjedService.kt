package no.nav.personbruker.dittnav.api.beskjed

import no.nav.personbruker.dittnav.api.common.MultiSourceResult
import no.nav.personbruker.dittnav.api.loginstatus.LoginLevelService
import no.nav.personbruker.dittnav.common.security.AuthenticatedUser
import org.slf4j.LoggerFactory

class BeskjedService(private val beskjedConsumer: BeskjedConsumer, private val loginLevelService: LoginLevelService) {

    private val log = LoggerFactory.getLogger(BeskjedService::class.java)

    private val kildetype = KildeType.EVENTHANDLER

    suspend fun getActiveBeskjedEvents(user: AuthenticatedUser): MultiSourceResult<BeskjedDTO, KildeType> {
        return getBeskjedEvents(user) {
            beskjedConsumer.getExternalActiveEvents(user)
        }
    }

    suspend fun getInactiveBeskjedEvents(user: AuthenticatedUser): MultiSourceResult<BeskjedDTO, KildeType> {
        return getBeskjedEvents(user) {
            beskjedConsumer.getExternalInactiveEvents(user)
        }
    }

    private suspend fun getBeskjedEvents(
        user: AuthenticatedUser,
        getEvents: suspend (AuthenticatedUser) -> List<Beskjed>
    ): MultiSourceResult<BeskjedDTO, KildeType> {
        return try {
            val externalEvents = getEvents(user)
            val highestRequiredLoginLevel = getHighestRequiredLoginLevel(externalEvents)
            val operatingLoginLevel = loginLevelService.getOperatingLoginLevel(user, highestRequiredLoginLevel)
            val results = externalEvents.map { beskjed -> transformToDTO(beskjed, operatingLoginLevel) }
            MultiSourceResult.createSuccessfulResult(results, kildetype)

        } catch (e: Exception) {
            log.warn("Klarte ikke å hente data fra $kildetype: $e", e)
            MultiSourceResult.createErrorResult(kildetype)
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
