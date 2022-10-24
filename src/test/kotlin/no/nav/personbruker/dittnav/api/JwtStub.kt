package no.nav.personbruker.dittnav.api

import com.auth0.jwk.Jwk
import com.auth0.jwk.JwkProvider
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import no.nav.personbruker.dittnav.api.authentication.AuthenticatedUser
import no.nav.security.token.support.core.jwt.JwtToken
import java.security.Key
import java.security.KeyPairGenerator
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.time.ZonedDateTime
import java.util.Base64
import java.util.Date

class JwtStub(private val issuer: String = "test issuer") {

    private val privateKey: RSAPrivateKey
    private val publicKey: RSAPublicKey

    init {
        val keyPairGenerator = KeyPairGenerator.getInstance("RSA")
        keyPairGenerator.initialize(512)

        val keyPair = keyPairGenerator.genKeyPair()
        privateKey = keyPair.private as RSAPrivateKey
        publicKey = keyPair.public as RSAPublicKey
    }

    fun createTokenFor(pid: String, audience: String = "", authLevel: String = "Level4"): String {
        val algorithm = Algorithm.RSA256(publicKey, privateKey)

        return JWT.create()
            .withIssuer(issuer)
            .withAudience(audience)
            .withClaim("pid", pid)
            .withClaim("acr",authLevel)
            .sign(algorithm)
    }

    fun stubbedJwkProvider(): StubbedJwkProvider {
        return StubbedJwkProvider(publicKey)
    }

    class StubbedJwkProvider(private val publicKey: RSAPublicKey) : JwkProvider {
        override fun get(keyId: String?): Jwk {
            return Jwk(
                keyId, "RSA", "RS256", "sig", listOf(), null, null, null,
                mapOf(
                    "e" to String(Base64.getEncoder().encode(publicKey.publicExponent.toByteArray())),
                    "n" to String(Base64.getEncoder().encode(publicKey.modulus.toByteArray()))
                )
            )
        }
    }
}

object TestUser {

    private val key: Key = Keys.secretKeyFor(SignatureAlgorithm.HS256)

    fun createAuthenticatedUser(): AuthenticatedUser {
        val ident = "12345"
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