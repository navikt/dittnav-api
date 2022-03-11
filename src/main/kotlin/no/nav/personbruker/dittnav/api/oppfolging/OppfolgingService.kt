package no.nav.personbruker.dittnav.api.oppfolging

import no.nav.personbruker.dittnav.api.tokenx.AccessToken
import no.nav.personbruker.dittnav.common.security.AuthenticatedUser

class OppfolgingService(private val oppfolgingConsumer: OppfolgingConsumer) {
    suspend fun getOppfolging(user: AuthenticatedUser): Oppfolging {
        val token = AccessToken(user.token)

        val oppfolging = oppfolgingConsumer.getOppfolgingStatus(token)

        return OppfolgingTransformer.toInternal(oppfolging)
    }
}
