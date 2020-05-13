package no.nav.personbruker.dittnav.api.common

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import no.nav.security.token.support.core.jwt.JwtToken
import java.security.Key
import java.util.*

object InnloggetBrukerObjectMother {

    private val key: Key = Keys.secretKeyFor(SignatureAlgorithm.HS256)

    fun createInnloggetBruker(): InnloggetBruker {
        val ident = "12345"
        return createInnloggetBruker(ident)
    }

    fun createInnloggetBruker(ident: String): InnloggetBruker {
        val innloggingsnivaa = 4
        return createInnloggetBruker(ident, innloggingsnivaa)
    }

    fun createInnloggetBruker(ident: String, innloggingsnivaa: Int): InnloggetBruker {
        val expiredDate = Date(System.currentTimeMillis().plus(1000000))
        val jws = Jwts.builder()
                .setSubject(ident)
                .addClaims(mutableMapOf(Pair("acr", "Level$innloggingsnivaa")) as Map<String, Any>?)
                .setExpiration(expiredDate)
                .signWith(key).compact()
        val token = JwtToken(jws)

        return InnloggetBruker(ident, innloggingsnivaa, token.tokenAsString, token.jwtTokenClaims.expirationTime)
    }

}
