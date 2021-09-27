package no.nav.personbruker.dittnav.api.saker

import no.nav.personbruker.dittnav.api.legacy.LegacyConsumer
import no.nav.personbruker.dittnav.api.unleash.UnleashService
import no.nav.personbruker.dittnav.common.security.AuthenticatedUser

class SakerService(
    private val mineSakerConsumer: MineSakerConsumer,
    private val legacyConsumer: LegacyConsumer,
    private val unleashService: UnleashService
) {

    suspend fun getSaker(user: AuthenticatedUser): List<SakstemaDTO> {
        return if (unleashService.mineSakerEnabled(user)) {
            mineSakerConsumer.hentSistEndret(user)

        } else {
            legacyConsumer.hentSiste(user)
        }
    }

    suspend fun getMineSaker(user: AuthenticatedUser): List<SakstemaDTO> {
        return mineSakerConsumer.hentSistEndret(user)
    }

    suspend fun getLegacySaker(user: AuthenticatedUser): List<SakstemaDTO> {
        return legacyConsumer.hentSiste(user)
    }

}
