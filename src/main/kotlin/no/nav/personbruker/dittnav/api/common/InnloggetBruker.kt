package no.nav.personbruker.dittnav.api.common

import no.nav.security.token.support.core.jwt.JwtToken

class InnloggetBruker(val token: JwtToken) {

    fun getBearerToken(): String {
        return "Bearer " + token.tokenAsString
    }

    fun getIdentFromToken(): String {
        val ident = token.jwtTokenClaims.getStringClaim("pid")
        if (ident == null) {
            return token.jwtTokenClaims.getStringClaim("sub")
        }
        return ident
    }

}