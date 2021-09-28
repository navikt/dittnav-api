package no.nav.personbruker.dittnav.api.saker

import no.nav.personbruker.dittnav.api.legacy.LegacyConsumer
import no.nav.personbruker.dittnav.api.unleash.UnleashService
import no.nav.personbruker.dittnav.common.security.AuthenticatedUser

class SakerService(
    private val mineSakerConsumer: MineSakerConsumer,
    private val legacyConsumer: LegacyConsumer,
    private val unleashService: UnleashService,
    private val urlResolver: SakerInnsynUrlResolver
) {

    suspend fun getSaker(user: AuthenticatedUser): SisteSakstemaDTO {
        return if (unleashService.mineSakerEnabled(user)) {
            val sakstemaer = mineSakerConsumer.hentSistEndret(user)
            SisteSakstemaDTO(sakstemaer, urlResolver.getMineSakerUrl())

        } else {
            val sakstemaer = legacyConsumer.hentSiste(user)
            SisteSakstemaDTO(sakstemaer, urlResolver.getSaksoversiktUrl())
        }
    }

}
