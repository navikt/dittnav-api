package no.nav.personbruker.dittnav.api.oppfolging

import no.nav.personbruker.dittnav.api.authentication.AuthenticatedUser
import no.nav.personbruker.dittnav.api.tokenx.AccessToken


class OppfolgingService(private val oppfolgingConsumer: OppfolgingConsumer) {
    suspend fun getOppfolging(user: AuthenticatedUser): Oppfolging {
        val token = AccessToken(user.token)

        val oppfolging = oppfolgingConsumer.getOppfolgingStatus(token)

        return oppfolging.toInternal()
    }
}
