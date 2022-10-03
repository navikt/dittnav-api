package no.nav.personbruker.dittnav.api.authentication

import java.time.Instant


data class AuthenticatedUser (
    val ident: String,
    val loginLevel: Int,
    val token: String,
) {

    fun createAuthenticationHeader(): String {
        return "Bearer $token"
    }
    override fun toString(): String {
        return "AuthenticatedUser(ident='***', loginLevel=$loginLevel, token='***')"
    }
}

enum class IdentityClaim(val claimName : String) {

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
