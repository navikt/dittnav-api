package no.nav.personbruker.dittnav.api.meldekort

import no.nav.personbruker.dittnav.api.authentication.AuthenticatedUser


class MeldekortService(
    private val meldekortConsumer: MeldekortConsumer,
    private val meldekortTokendings: MeldekortTokendings
) {

    suspend fun getMeldekortInfo(user: AuthenticatedUser): Meldekortinfo {
        val token = meldekortTokendings.exchangeToken(user)
        return meldekortConsumer.getMeldekortStatus(token).toInternal()
    }

    suspend fun getMeldekortStatus(user: AuthenticatedUser): MeldekortstatusExternal {
        val token = meldekortTokendings.exchangeToken(user)

        return meldekortConsumer.getMeldekortStatus(token)
    }
}
