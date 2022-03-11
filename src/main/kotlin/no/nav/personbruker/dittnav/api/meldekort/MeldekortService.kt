package no.nav.personbruker.dittnav.api.meldekort

import no.nav.personbruker.dittnav.api.tokenx.AccessToken
import no.nav.personbruker.dittnav.common.security.AuthenticatedUser

class MeldekortService(private val meldekortConsumer: MeldekortConsumer) {

    suspend fun getMeldekortInfo(user: AuthenticatedUser): Meldekortinfo {
        val token = AccessToken(user.token)

        val meldekortStatus = meldekortConsumer.getMeldekortStatus(token)

        return MeldekortTransformer.toInternal(meldekortStatus)
    }
}
