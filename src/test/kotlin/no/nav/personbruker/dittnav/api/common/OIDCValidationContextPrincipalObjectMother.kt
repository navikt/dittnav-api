package no.nav.personbruker.dittnav.api.common

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import no.nav.security.token.support.core.context.TokenValidationContext
import no.nav.security.token.support.core.jwt.JwtToken
import no.nav.security.token.support.ktor.OIDCValidationContextPrincipal
import java.security.Key
import java.time.Instant
import java.util.*

object OIDCValidationContextPrincipalObjectMother {

    fun createPrincipalForAzure(ident: String, innloggingsnivaa: Int): OIDCValidationContextPrincipal {
        val key: Key = Keys.secretKeyFor(SignatureAlgorithm.HS256)
        val inTwoMinutes = Date(Instant.now().plusSeconds(60).toEpochMilli())
        val expiredJws = Jwts.builder()
                .setSubject(ident)
                .setExpiration(inTwoMinutes)
                .addClaims(
                        mutableMapOf(
                                Pair("acr", "Level$innloggingsnivaa"
                                )) as Map<String, Any>?)
                .signWith(key).compact()

        val token = JwtToken(expiredJws)

        val issuerShortNameValidatedTokenMap = mutableMapOf<String, JwtToken>()
        val issuer = "keyForIssuer"
        issuerShortNameValidatedTokenMap[issuer] = token
        val context = TokenValidationContext(issuerShortNameValidatedTokenMap)
        return OIDCValidationContextPrincipal(context)
    }

    fun createPrincipalForIdPorten(ident: String, innloggingsnivaa: Int): OIDCValidationContextPrincipal {
        val key: Key = Keys.secretKeyFor(SignatureAlgorithm.HS256)
        val inTwoMinutes = Date(Instant.now().plusSeconds(60).toEpochMilli())
        val expiredJws = Jwts.builder()
                .setExpiration(inTwoMinutes)
                .addClaims(
                        mutableMapOf(
                                Pair("acr", "Level$innloggingsnivaa"),
                                Pair("pid", ident)
                        ) as Map<String, Any>?)
                .signWith(key).compact()

        val token = JwtToken(expiredJws)

        val issuerShortNameValidatedTokenMap = mutableMapOf<String, JwtToken>()
        val issuer = "keyForIssuer"
        issuerShortNameValidatedTokenMap[issuer] = token
        val context = TokenValidationContext(issuerShortNameValidatedTokenMap)
        return OIDCValidationContextPrincipal(context)
    }

}