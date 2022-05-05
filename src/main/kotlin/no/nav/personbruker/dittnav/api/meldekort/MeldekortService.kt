package no.nav.personbruker.dittnav.api.meldekort

import no.nav.personbruker.dittnav.api.meldekort.external.MeldekortstatusExternal
import no.nav.personbruker.dittnav.common.security.AuthenticatedUser

class MeldekortService(
    private val meldekortConsumer: MeldekortConsumer,
    private val meldekortTokendings: MeldekortTokendings
) {

    suspend fun getMeldekortInfo(user: AuthenticatedUser): Meldekortinfo {
        val token = meldekortTokendings.exchangeToken(user)

        val meldekortStatus = meldekortConsumer.getMeldekortStatus(token)

        return MeldekortTransformer.toInternal(meldekortStatus)
    }

    suspend fun getMeldekortStatus(user: AuthenticatedUser): MeldekortstatusExternal {
        val token = meldekortTokendings.exchangeToken(user)

        return meldekortConsumer.getMeldekortStatus(token)
    }
}
