package no.nav.personbruker.dittnav.api.common

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

data class InnloggetBruker(
    val ident: String,
    val innloggingsnivaa: Int,
    val token: String,
    val tokenExpirationTime: LocalDateTime
) {

    private val oslo = ZoneId.of("Europe/Oslo")

    fun createAuthenticationHeader(): String {
        return "Bearer $token"
    }

    override fun toString(): String {
        return "InnloggetBruker(ident='***', innloggingsnivaa=$innloggingsnivaa, token='***', tokenUtlopsdato: $tokenExpirationTime)"
    }

    fun isTokenExpired(): Boolean {
        val now = Instant.now().atZone(oslo).toLocalDateTime()
        return tokenExpirationTime.isBefore(now)
    }

    fun isTokenAboutToExpire(expiryThresholdInMinutes: Long): Boolean {
        val now = Instant.now().atZone(oslo).toLocalDateTime()
        return now.isAfter(tokenExpirationTime.minusMinutes(expiryThresholdInMinutes))
    }

}
