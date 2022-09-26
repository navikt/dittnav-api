package no.nav.personbruker.dittnav.api.authentication

import com.auth0.jwt.interfaces.Payload
import io.ktor.application.ApplicationCall
import io.ktor.auth.Principal
import io.ktor.auth.principal
import no.nav.security.token.support.core.jwt.JwtToken
import no.nav.security.token.support.ktor.TokenValidationContextPrincipal
import java.time.Instant


object AuthenticatedUserFactory {

    private val IDENT_CLAIM: IdentityClaim
    private val defaultClaim = IdentityClaim.SUBJECT
    private val oidcIdentityClaimName = "OIDC_CLAIM_CONTAINING_THE_IDENTITY"

    private val NAV_ESSO_COOKIE_NAME = "nav-esso"

    init {
        val identityClaimFromEnvVariable = System.getenv(oidcIdentityClaimName) ?: defaultClaim.claimName
        IDENT_CLAIM = IdentityClaim.fromClaimName(identityClaimFromEnvVariable)
    }
    fun createNewAuthenticatedUser(principal: PrincipalWithTokenString, essoToken: String? = null): AuthenticatedUser {
        val token = principal.accessToken

        val ident: String = principal.payload.getClaim("pid").asString()//jwtTokenClaims.getStringClaim(IDENT_CLAIM.claimName)
        val loginLevel =
            extractLoginLevel( principal.payload)

        return AuthenticatedUser(ident, loginLevel, principal.accessToken)
    }

    fun createNewAuthenticatedUser(call: ApplicationCall): AuthenticatedUser {
        val principal = call.principal<PrincipalWithTokenString>()
            ?: throw Exception("Principal har ikke blitt satt for authentication context.")

        val essoToken = getEssoTokenIfPresent(call)

        return createNewAuthenticatedUser(principal, essoToken)
    }

    private fun extractLoginLevel(payload: Payload): Int {

        return when (payload.getClaim("acr").asString()) {
            "Level3" -> 3
            "Level4" -> 4
            else -> throw Exception("Innloggingsnivå ble ikke funnet. Dette skal ikke kunne skje.")
        }
    }

    private fun getTokenExpirationLocalDateTime(token: JwtToken): Instant {
        return token.jwtTokenClaims
            .expirationTime
            .toInstant()
    }

    private fun getEssoTokenIfPresent(call: ApplicationCall): String? {
        return call.request.cookies[NAV_ESSO_COOKIE_NAME]
            ?: call.request.headers[NAV_ESSO_COOKIE_NAME]
    }

}
data class  PrincipalWithTokenString(val accessToken:String, val payload:Payload): Principal