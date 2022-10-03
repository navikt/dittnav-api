package no.nav.personbruker.dittnav.api.authentication

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import no.nav.security.token.support.core.jwt.JwtToken
import java.security.Key
import java.util.Date

object AuthenticatedUserObjectMother {

    private val key: Key = Keys.secretKeyFor(SignatureAlgorithm.HS256)

    fun createAuthenticatedUser(): AuthenticatedUser {
        val ident = "12345"
        return createAuthenticatedUser(ident)
    }

    fun createAuthenticatedUser(ident: String): AuthenticatedUser {
        val loginLevel = 4
        return createAuthenticatedUser(ident, loginLevel)
    }

    fun createAuthenticatedUser(ident: String, loginLevel: Int): AuthenticatedUser {
        val jws = Jwts.builder()
            .setSubject(ident)
            .addClaims(mutableMapOf(Pair("acr", "Level$loginLevel")) as Map<String, Any>?)
            .setExpiration(Date(System.currentTimeMillis().plus(1000000)))
            .signWith(key).compact()
        val token = JwtToken(jws)
        return AuthenticatedUser(ident, loginLevel, token.tokenAsString)
    }

}
