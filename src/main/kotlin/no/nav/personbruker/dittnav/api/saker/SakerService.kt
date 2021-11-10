package no.nav.personbruker.dittnav.api.saker

import no.nav.personbruker.dittnav.api.common.ConsumeSakerException
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

    suspend fun hentSisteToEndredeSakstemaer(user: AuthenticatedUser): SakerDTO {
        try {
            return if (unleashService.mineSakerEnabled(user)) {
                val exchangedToken = mineSakerTokendings.exchangeToken(user)
                val sisteSakstemaer = mineSakerConsumer.hentSistEndret(exchangedToken)
                SakerDTO(sisteSakstemaer.sakstemaer, urlResolver.getMineSakerUrl(), sisteSakstemaer.dagpengerSistEndret)

            } else {
                val sisteSakstemaer = legacyConsumer.hentSisteEndret(user)
                SakerDTO(
                    sisteSakstemaer.sakstemaer,
                    urlResolver.getSaksoversiktUrl(),
                    sisteSakstemaer.dagpengerSistEndret
                )
            }
        } catch (e: Exception) {
            throw ConsumeSakerException("Klarte ikke å hente info om saker", e)
        }
    }

}
