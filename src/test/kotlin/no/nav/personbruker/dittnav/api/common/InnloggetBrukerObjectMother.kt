package no.nav.personbruker.dittnav.api.common

import io.mockk.every
import io.mockk.mockk
import no.nav.security.token.support.core.context.TokenValidationContext
import no.nav.security.token.support.ktor.OIDCValidationContextPrincipal

fun createInnloggetBruker(): InnloggetBruker {
    val innloggetBruker = mockk<InnloggetBruker>()
    every { innloggetBruker.getBearerToken() } returns "dummyToken"
    return innloggetBruker
}

fun createDummyTokenSupport(): OIDCValidationContextPrincipal {
    val tokenValidationContext = mockk<TokenValidationContext>()
    return OIDCValidationContextPrincipal(tokenValidationContext)
}