package no.nav.personbruker.dittnav.api.unleash

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import no.finn.unleash.Unleash
import no.finn.unleash.UnleashContext
import no.nav.personbruker.dittnav.common.security.AuthenticatedUser

class UnleashService(private val unleashClient: Unleash) {

    companion object {
        const val digiSosOppgaveToggleName: String = "digiSosEnabled"
        const val digisosPaabegynteToggleName: String = "digisosPaabegynteEnabled"
        const val varselinnboksToggleName: String = "mergeBeskjedVarselEnabled"
        const val situasjonToggleName: String = "veientilarbeid.kanViseUtfraSituasjon"
        const val minSideToggleName: String = "minSideEnabled"
    }

    suspend fun mergeBeskjedVarselEnabled(user: AuthenticatedUser): Boolean {
        return withContext(Dispatchers.IO) {
            unleashClient.isEnabled(varselinnboksToggleName, createUnleashContext(user), false)
        }
    }

    suspend fun digiSosOppgaveEnabled(user: AuthenticatedUser): Boolean = withContext(Dispatchers.IO) {
        unleashClient.isEnabled(digiSosOppgaveToggleName, createUnleashContext(user), false)
    }

    suspend fun digiSosPaabegynteEnabled(user: AuthenticatedUser): Boolean = withContext(Dispatchers.IO) {
        unleashClient.isEnabled(digisosPaabegynteToggleName, createUnleashContext(user), false)
    }

    suspend fun situasjonEnabled(user: AuthenticatedUser): Boolean = withContext(Dispatchers.IO) {
        unleashClient.isEnabled(situasjonToggleName, createUnleashContext(user), false)
    }

    suspend fun minSideEnabled(user: AuthenticatedUser): Boolean = withContext(Dispatchers.IO) {
        unleashClient.isEnabled(minSideToggleName, createUnleashContext(user), false)
    }

    private fun createUnleashContext(user: AuthenticatedUser): UnleashContext {
        return UnleashContext.builder()
            .userId(user.ident)
            .build()
    }

}
