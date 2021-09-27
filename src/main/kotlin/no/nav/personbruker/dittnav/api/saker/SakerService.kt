package no.nav.personbruker.dittnav.api.saker

import no.nav.personbruker.dittnav.api.legacy.LegacyConsumer
import no.nav.personbruker.dittnav.api.unleash.UnleashService
import no.nav.personbruker.dittnav.common.security.AuthenticatedUser

class SakerService(
    private val mineSakerClient: MineSakerClient,
    private val legacyConsumer: LegacyConsumer,
    private val unleashService: UnleashService
) {

    suspend fun getSaker(user: AuthenticatedUser): List<SakerDTO> {
        if (unleashService.sakerEnabled(user)) {
            val externalSaker = mineSakerClient.getExternalSaker(user)
            return externalSaker.map { sak -> toSakerDTO(sak) }
        }

        val externalSaker = legacyConsumer.hentSiste(user)
        return externalSaker.sakstemaList.map { sak -> toSakerDTOFromLegacy(sak) }
    }

    suspend fun getMineSaker(user: AuthenticatedUser): List<SakerDTO> {
        val externalSaker = mineSakerClient.getExternalSaker(user)
        return externalSaker.map { sak -> toSakerDTO(sak) }
    }

    suspend fun getLegacySaker(user: AuthenticatedUser): List<SakerDTO> {
        val externalSaker = legacyConsumer.hentSiste(user)
        return externalSaker.sakstemaList.map { sak -> toSakerDTOFromLegacy(sak) }
    }

}
