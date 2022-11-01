package no.nav.personbruker.dittnav.api.saker

import no.nav.personbruker.dittnav.api.authentication.AuthenticatedUser
import no.nav.tms.token.support.tokendings.exchange.TokendingsService

class MineSakerTokendings(
    private val tokendingsService: TokendingsService,
    private val mineSakerClientId: String
) {
    suspend fun exchangeToken(authenticatedUser: AuthenticatedUser) =
        tokendingsService.exchangeToken(authenticatedUser.token, mineSakerClientId)

}
