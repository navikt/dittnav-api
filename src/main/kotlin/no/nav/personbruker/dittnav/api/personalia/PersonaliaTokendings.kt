package no.nav.personbruker.dittnav.api.personalia

import no.nav.personbruker.dittnav.api.tokenx.AccessToken
import no.nav.personbruker.dittnav.common.security.AuthenticatedUser
import no.nav.tms.token.support.tokendings.exchange.TokendingsService

class PersonaliaTokendings(
    private val tokendingsService: TokendingsService,
    private val personaliaClientId: String
) {

    suspend fun exchangeToken(authenticatedUser: AuthenticatedUser): AccessToken {
        return AccessToken(tokendingsService.exchangeToken(authenticatedUser.token, personaliaClientId))
    }
}
