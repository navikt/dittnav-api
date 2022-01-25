package no.nav.personbruker.dittnav.api.common

import java.util.*

enum class IdentityClaim(val claimName: String) {

    SUBJECT("sub"),
    PID("pid");

    companion object {
        fun fromClaimName(claimName: String): IdentityClaim {
            values().forEach { currentClaim ->
                if (currentClaim.claimName == claimName.lowercase(Locale.getDefault())) {
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
