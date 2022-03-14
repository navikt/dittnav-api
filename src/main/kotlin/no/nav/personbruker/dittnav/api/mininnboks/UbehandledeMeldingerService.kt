package no.nav.personbruker.dittnav.api.mininnboks

import no.nav.personbruker.dittnav.api.tokenx.AccessToken
import no.nav.personbruker.dittnav.common.security.AuthenticatedUser

class UbehandledeMeldingerService(
    private val ubehandledeMeldingerConsumer: MininnboksConsumer,
    private val ubehdandledeMeldingerTransformer: UbehandledeMeldingerTransformer
) {
    suspend fun getUbehandledeMeldinger(user: AuthenticatedUser): List<UbehandledeMeldinger> {
        val token = AccessToken(user.token)

        val oppfolging = ubehandledeMeldingerConsumer.getUbehandledeMeldinger(token)

        return ubehdandledeMeldingerTransformer.toInternal(oppfolging)
    }
}
