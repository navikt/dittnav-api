package no.nav.personbruker.dittnav.api.common

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import no.nav.security.token.support.core.jwt.JwtToken
import java.security.Key
import java.time.ZoneId
import java.time.ZonedDateTime
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
        val jws = Jwts.builder()
                .setSubject(ident)
                .addClaims(mutableMapOf(Pair("acr", "Level$innloggingsnivaa")) as Map<String, Any>?)
                .setExpiration(Date(System.currentTimeMillis().plus(1000000)))
                .signWith(key).compact()
        val token = JwtToken(jws)
        val expirationTime = token.jwtTokenClaims
                                                .expirationTime
                                                .toInstant()
                                                .atZone(ZoneId.of("Europe/Oslo"))
                                                .toLocalDateTime()
        return InnloggetBruker(ident, innloggingsnivaa, token.tokenAsString, expirationTime)
    }

    fun createInnloggetBruker(ident: String, innloggingsnivaa: Int, tokensUtlopstidspunkt: ZonedDateTime): InnloggetBruker {
        val jws = Jwts.builder()
                .setSubject(ident)
                .addClaims(mutableMapOf(Pair("acr", "Level$innloggingsnivaa")) as Map<String, Any>?)
                .setExpiration(Date.from(tokensUtlopstidspunkt.toInstant()))
                .signWith(key).compact()
        val token = JwtToken(jws)
        val expirationTime = token.jwtTokenClaims
                                                .expirationTime
                                                .toInstant()
                                                .atZone(ZoneId.of("Europe/Oslo"))
                                                .toLocalDateTime()
        return InnloggetBruker(ident, innloggingsnivaa, token.tokenAsString, expirationTime)
    }

}
