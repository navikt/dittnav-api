package no.nav.personbruker.dittnav.api.meldekort

import no.nav.personbruker.dittnav.api.authentication.AuthenticatedUser
import no.nav.personbruker.dittnav.api.tokenx.AccessToken
import no.nav.tms.token.support.tokendings.exchange.TokendingsService

class MeldekortTokendings(
    private val tokendingsService: TokendingsService,
    private val meldekortClientId: String
) {

    suspend fun exchangeToken(authenticatedUser: AuthenticatedUser): AccessToken {
        return AccessToken(tokendingsService.exchangeToken(authenticatedUser.token, meldekortClientId))
    }
}
