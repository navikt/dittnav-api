package no.nav.personbruker.dittnav.api.authentication

import java.time.Instant
import java.time.temporal.ChronoUnit

data class AuthenticatedUser (
    val ident: String,
    val loginLevel: Int,
    val token: String,
    val tokenExpirationTime: Instant,
    val auxiliaryEssoToken: String? = null
) {

    fun createAuthenticationHeader(): String {
        return "Bearer $token"
    }

    override fun toString(): String {
        return "AuthenticatedUser(ident='***', loginLevel=$loginLevel, token='***', expiryTime=$tokenExpirationTime, expired=${isTokenExpired()})"
    }

    fun isTokenExpired(): Boolean {
        val now = Instant.now()
        return tokenExpirationTime.isBefore(now)
    }

    fun isTokenAboutToExpire(expiryThresholdInSeconds: Long): Boolean {
        val now = Instant.now()
        return now.isAfter(tokenExpirationTime.minus(expiryThresholdInSeconds, ChronoUnit.SECONDS))
    }
}

enum class IdentityClaim(val claimName : String) {

    SUBJECT("sub"),
    PID("pid");

    companion object {
        fun fromClaimName(claimName: String): IdentityClaim {
            values().forEach { currentClaim ->
                if (currentClaim.claimName == claimName.lowercase()) {
                    return currentClaim
                }
            }
            val msg = "Ugyldig claim name '$claimName', gyldige verdier er ${values().toSet()}"
            throw IllegalArgumentException(msg)
        }
    }

    override fun toString(): String {
        return "IdentidyClaim(claimName='$claimName')"
    }

}
