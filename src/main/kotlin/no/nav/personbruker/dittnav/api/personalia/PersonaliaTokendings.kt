package no.nav.personbruker.dittnav.api.personalia

import no.nav.personbruker.dittnav.api.authentication.AuthenticatedUser

import no.nav.tms.token.support.tokendings.exchange.TokendingsService

class PersonaliaTokendings(
    private val tokendingsService: TokendingsService,
    private val personaliaClientId: String
) {

    suspend fun exchangeToken(authenticatedUser: AuthenticatedUser) =
        tokendingsService.exchangeToken(authenticatedUser.token, personaliaClientId)
}
