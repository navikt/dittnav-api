package no.nav.personbruker.dittnav.api.tokenx

import no.nav.personbruker.dittnav.common.security.AuthenticatedUser
import no.nav.tms.token.support.tokendings.exchange.TokendingsService

class EventhandlerTokendings(
    private val tokendingsService: TokendingsService,
    private val eventhandlerClientId: String
) {
    suspend fun exchangeToken(authenticatedUser: AuthenticatedUser): AccessToken {
        return AccessToken(tokendingsService.exchangeToken(authenticatedUser.token, eventhandlerClientId))
    }
}
