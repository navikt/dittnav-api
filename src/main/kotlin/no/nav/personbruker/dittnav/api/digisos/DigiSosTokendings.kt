package no.nav.personbruker.dittnav.api.digisos

import no.nav.personbruker.dittnav.api.authentication.AuthenticatedUser
import no.nav.tms.token.support.tokendings.exchange.TokendingsService

class DigiSosTokendings(
    private val tokendingsService: TokendingsService,
    private val digiSosClientId: String
) {
    suspend fun exchangeToken(authenticatedUser: AuthenticatedUser): String =
        tokendingsService.exchangeToken(authenticatedUser.token, digiSosClientId)
}
