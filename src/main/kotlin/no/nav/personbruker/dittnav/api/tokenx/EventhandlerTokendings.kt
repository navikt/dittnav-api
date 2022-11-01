package no.nav.personbruker.dittnav.api.tokenx


import no.nav.personbruker.dittnav.api.authentication.AuthenticatedUser
import no.nav.tms.token.support.tokendings.exchange.TokendingsService

class EventhandlerTokendings(
    private val tokendingsService: TokendingsService,
    private val eventhandlerClientId: String
) {
    suspend fun exchangeToken(authenticatedUser: AuthenticatedUser)=
        tokendingsService.exchangeToken(authenticatedUser.token, eventhandlerClientId)

}
