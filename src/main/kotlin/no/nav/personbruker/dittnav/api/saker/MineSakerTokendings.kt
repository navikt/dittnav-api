package no.nav.personbruker.dittnav.api.saker

import no.nav.personbruker.dittnav.api.authentication.AuthenticatedUser
import no.nav.personbruker.dittnav.api.tokenx.AccessToken
import no.nav.tms.token.support.tokendings.exchange.TokendingsService

class MineSakerTokendings(
    private val tokendingsService: TokendingsService,
    private val mineSakerClientId: String
) {
    suspend fun exchangeToken(authenticatedUser: AuthenticatedUser): AccessToken {
        return AccessToken(tokendingsService.exchangeToken(authenticatedUser.token, mineSakerClientId))
    }
}
