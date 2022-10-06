package no.nav.personbruker.dittnav.api.saker

import no.nav.personbruker.dittnav.api.authentication.AuthenticatedUser
import no.nav.personbruker.dittnav.api.common.ConsumeSakerException
import java.net.URL

class SakerService(
    private val mineSakerConsumer: MineSakerConsumer,
    private val mineSakerUrl: URL,
    private val mineSakerTokendings: MineSakerTokendings
) {

    suspend fun hentSistEndredeSakstemaer(user: AuthenticatedUser): SakerDTO {
        return try {
            val exchangedToken = mineSakerTokendings.exchangeToken(user)
            val sisteSakstemaer = mineSakerConsumer.hentSistEndret(exchangedToken)
            SakerDTO(sisteSakstemaer.sakstemaer, mineSakerUrl, sisteSakstemaer.dagpengerSistEndret)
        } catch (e: Exception) {
            throw ConsumeSakerException("Klarte ikke å hente info om saker", e)
        }
    }

}
