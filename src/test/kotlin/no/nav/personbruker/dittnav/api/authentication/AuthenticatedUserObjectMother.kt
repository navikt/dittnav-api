package no.nav.personbruker.dittnav.api.authentication

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import no.nav.security.token.support.core.jwt.JwtToken
import java.security.Key
import java.time.ZonedDateTime
import java.util.*

object AuthenticatedUserObjectMother {

    private val key: Key = Keys.secretKeyFor(SignatureAlgorithm.HS256)

    fun createAuthenticatedUser(): AuthenticatedUser {
        val ident = "12345"
        return createAuthenticatedUser(ident)
    }

    fun createAuthenticatedUser(ident: String): AuthenticatedUser {
        val innloggingsnivaa = 4
        return createAuthenticatedUser(ident, innloggingsnivaa)
    }

    fun createAuthenticatedUser(ident: String, innloggingsnivaa: Int): AuthenticatedUser {
        val inTwoMinutes = ZonedDateTime.now().plusMinutes(2)
        val jws = Jwts.builder()
            .setSubject(ident)
            .addClaims(mutableMapOf(Pair("acr", "Level$innloggingsnivaa")) as Map<String, Any>?)
            .setExpiration(Date.from(inTwoMinutes.toInstant()))
            .signWith(key).compact()
        val token = JwtToken(jws)
        return AuthenticatedUser(ident, innloggingsnivaa, token.tokenAsString)
    }
}
