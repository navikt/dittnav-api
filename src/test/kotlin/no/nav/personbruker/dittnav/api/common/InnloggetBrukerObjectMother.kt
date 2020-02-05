package no.nav.personbruker.dittnav.api.common

import io.mockk.every
import io.mockk.mockk
import no.nav.security.token.support.core.context.TokenValidationContext
import no.nav.security.token.support.core.jwt.JwtToken
import no.nav.security.token.support.ktor.OIDCValidationContextPrincipal

object InnloggetBrukerObjectMother {

    fun createInnloggetBruker(): InnloggetBruker {
        val innloggetBruker = mockk<InnloggetBruker>()
        every { innloggetBruker.getBearerToken() } returns "dummyToken"
        return innloggetBruker
    }

    fun createDummyTokenSupport(): OIDCValidationContextPrincipal {
        val tokenValidationContext = mockk<TokenValidationContext>()
        val oidcValidationContextPrincipal = OIDCValidationContextPrincipal(tokenValidationContext)
        val dummyJwtToken = mockk<JwtToken>()

        every { oidcValidationContextPrincipal.context.firstValidToken.get() } returns dummyJwtToken

        return oidcValidationContextPrincipal
    }
}