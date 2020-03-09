package no.nav.personbruker.dittnav.api.common

import io.mockk.every
import io.mockk.mockk
import no.nav.security.token.support.core.jwt.JwtToken

object InnloggetBrukerObjectMother {

    fun createInnloggetBruker(securityLevel: SecurityLevel): InnloggetBruker {
        val dummyJwtToken = mockk<JwtToken>()
        val dummyTokenAsString = "dummyToken"
        every { dummyJwtToken.tokenAsString} returns dummyTokenAsString
        every { dummyJwtToken.jwtTokenClaims.getStringClaim("acr")} returns securityLevel.toString()
        return InnloggetBruker(dummyJwtToken)
    }
}
