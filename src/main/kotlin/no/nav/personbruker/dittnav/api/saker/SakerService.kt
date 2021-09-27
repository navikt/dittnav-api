package no.nav.personbruker.dittnav.api.saker

import no.nav.personbruker.dittnav.api.unleash.UnleashService
import no.nav.personbruker.dittnav.common.security.AuthenticatedUser

class SakerService(
        private val sakerClient: SakerClient,
        private val unleashService: UnleashService
) {

    suspend fun getSaker(user: AuthenticatedUser): List<SakerDTO> {
        if (unleashService.sakerEnabled(user)) {
            val externalSaker = sakerClient.getExternalSaker(user)
            return externalSaker.map { sak -> toSakerDTO(sak) }
        }

        val externalSaker = sakerClient.getExternalLegacySaker(user)
        return externalSaker.sakstemaList.map { sak -> toSakerDTOFromLegacy(sak) }
    }

    suspend fun getMineSaker(user: AuthenticatedUser): List<SakerDTO> {
        val externalSaker = sakerClient.getExternalSaker(user)
        return externalSaker.map { sak -> toSakerDTO(sak) }
    }

}
