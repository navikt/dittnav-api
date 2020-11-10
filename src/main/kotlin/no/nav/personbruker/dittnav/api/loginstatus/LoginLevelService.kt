package no.nav.personbruker.dittnav.api.loginstatus

import no.nav.personbruker.dittnav.common.logging.util.createClassLogger
import no.nav.personbruker.dittnav.common.security.AuthenticatedUser

class LoginLevelService(private val innloggingsstatusConsumer: InnloggingsstatusConsumer) {

    val log = createClassLogger()

    suspend fun getOperatingLoginLevel(user: AuthenticatedUser, highestRequiredLevel: Int): Int {
        return when {
            providedLoginLevelIsSufficient(user, highestRequiredLevel) -> user.loginLevel
            userHasPossibleStepUp(user) -> fetchOperatingLoginLevel(user)
            else -> user.loginLevel
        }
    }

    private fun providedLoginLevelIsSufficient(user: AuthenticatedUser, requiredLevel: Int): Boolean {
        return user.loginLevel >= requiredLevel
    }

    private fun userHasPossibleStepUp(user: AuthenticatedUser): Boolean {
        return user.auxiliaryEssoToken != null
    }

    private suspend fun fetchOperatingLoginLevel(user: AuthenticatedUser): Int {
        return try {
            val summary = innloggingsstatusConsumer.fetchAuthSummary(user)

            if (summary.authenticated && summary.authLevel != null) {
                summary.authLevel
            } else {
                user.loginLevel
            }
        } catch (e: Exception) {
            log.warn("Klarte ikke hente detaljert innloggingsstatus. Bruker innloggingssnivå gitt fra oidc-token.")
            user.loginLevel
        }
    }
}