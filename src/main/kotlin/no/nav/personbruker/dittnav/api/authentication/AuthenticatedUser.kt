package no.nav.personbruker.dittnav.api.authentication

import com.auth0.jwt.interfaces.Payload
import io.ktor.server.application.ApplicationCall
import io.ktor.server.auth.Principal
import io.ktor.server.auth.principal

data class AuthenticatedUser (
    val ident: String,
    val loginLevel: Int,
    val token: String,
)

object AuthenticatedUserFactory {

    private fun createNewAuthenticatedUser(principal: PrincipalWithTokenString): AuthenticatedUser {

        val ident: String = principal.payload.getClaim("pid").asString()
        val loginLevel =
            extractLoginLevel(principal.payload)
        return AuthenticatedUser(ident, loginLevel, principal.accessToken)
    }

    fun createNewAuthenticatedUser(call: ApplicationCall): AuthenticatedUser {
        val principal = call.principal<PrincipalWithTokenString>()
            ?: throw Exception("Principal har ikke blitt satt for authentication context.")

        return createNewAuthenticatedUser(principal)
    }

    private fun extractLoginLevel(payload: Payload): Int {

        return when (payload.getClaim("acr").asString()) {
            "Level3" -> 3
            "Level4" -> 4
            else -> throw Exception("Innloggingsnivå ble ikke funnet. Dette skal ikke kunne skje.")
        }
    }

}

data class PrincipalWithTokenString(val accessToken: String, val payload: Payload) : Principal