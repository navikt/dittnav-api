package no.nav.personbruker.dittnav.api.common

import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.auth.authentication
import io.ktor.util.pipeline.PipelineContext
import no.nav.security.token.support.core.jwt.JwtToken
import no.nav.security.token.support.ktor.OIDCValidationContextPrincipal

class InnloggetBruker(val token: JwtToken) {

    fun getBearerToken(): String {
        return "Bearer " + token.tokenAsString
    }

    fun getIdentFromToken(): String {
        val ident = token.jwtTokenClaims.getStringClaim("sub")

        if (isClaimElevenDigitsOrLess(ident)) {
            return ident
        }
        return token.jwtTokenClaims.getStringClaim("pid")
    }

    private fun isClaimElevenDigitsOrLess(ident: String): Boolean {
        val regex = """\d{1,11}""".toRegex()
        return regex.matches(ident)
    }
}

val PipelineContext<Unit, ApplicationCall>.innloggetBruker: InnloggetBruker get() =
    call.authentication.principal<OIDCValidationContextPrincipal>()
            ?.context
            ?.firstValidToken
            ?.map { token -> InnloggetBruker(token) }
            ?.get()
            ?: throw Exception("Det ble ikke funnet noe token. Dette skal ikke kunne skje.")