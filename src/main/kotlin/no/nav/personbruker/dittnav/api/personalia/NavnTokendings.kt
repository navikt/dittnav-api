package no.nav.personbruker.dittnav.api.personalia

import no.nav.personbruker.dittnav.api.authentication.AuthenticatedUser
import no.nav.personbruker.dittnav.api.tokenx.AccessToken

import no.nav.tms.token.support.tokendings.exchange.TokendingsService

class NavnTokendings(
    private val tokendingsService: TokendingsService,
    private val pdlClientId: String
) {

    suspend fun exchangeToken(authenticatedUser: AuthenticatedUser): AccessToken {
        return AccessToken(tokendingsService.exchangeToken(authenticatedUser.token, pdlClientId))
    }
}
