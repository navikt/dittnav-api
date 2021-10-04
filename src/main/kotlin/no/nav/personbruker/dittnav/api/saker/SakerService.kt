package no.nav.personbruker.dittnav.api.saker

import no.nav.personbruker.dittnav.api.legacy.LegacyConsumer
import no.nav.personbruker.dittnav.api.unleash.UnleashService
import no.nav.personbruker.dittnav.common.security.AuthenticatedUser

class SakerService(
    private val mineSakerConsumer: MineSakerConsumer,
    private val legacyConsumer: LegacyConsumer,
    private val unleashService: UnleashService,
    private val urlResolver: SakerInnsynUrlResolver,
    private val mineSakerTokendings: MineSakerTokendings
) {

    suspend fun hentSisteToEndredeSakstemaer(user: AuthenticatedUser): SisteSakstemaDTO {
        return if (unleashService.mineSakerEnabled(user)) {
            val exchangedToken = mineSakerTokendings.exchangeToken(user)
            val sakstemaer = mineSakerConsumer.hentSistEndret(exchangedToken)
            SisteSakstemaDTO(sakstemaer, urlResolver.getMineSakerUrl())

        } else {
            val sakstemaer = legacyConsumer.hentSisteEndret(user)
            SisteSakstemaDTO(sakstemaer, urlResolver.getSaksoversiktUrl())
        }
    }

}
