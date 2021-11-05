package no.nav.personbruker.dittnav.api.unleash

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import no.finn.unleash.Unleash
import no.finn.unleash.UnleashContext
import no.nav.personbruker.dittnav.common.security.AuthenticatedUser

class UnleashService(private val unleashClient: Unleash) {

    companion object {
        const val digiSosToggleName: String = "digiSosEnabled"
        const val varselinnboksToggleName: String = "mergeBeskjedVarselEnabled"
        const val brukMineSakerToggleName: String = "dittnav.brukMineSaker"
        const val innboksToggleName: String = "dittnav.innboks"
        const val situasjonToggleName: String = "veientilarbeid.kanViseUtfraSituasjon"
    }

    suspend fun mergeBeskjedVarselEnabled(user: AuthenticatedUser): Boolean {
        return withContext(Dispatchers.IO) {
            unleashClient.isEnabled(varselinnboksToggleName, createUnleashContext(user), false)
        }
    }

    suspend fun digiSosEnabled(user: AuthenticatedUser): Boolean = withContext(Dispatchers.IO) {
        unleashClient.isEnabled(digiSosToggleName, createUnleashContext(user), false)
    }

    suspend fun mineSakerEnabled(user: AuthenticatedUser): Boolean = withContext(Dispatchers.IO) {
        unleashClient.isEnabled(brukMineSakerToggleName, createUnleashContext(user), false)
    }

    suspend fun innboksEnabled(user: AuthenticatedUser): Boolean = withContext(Dispatchers.IO) {
        unleashClient.isEnabled(innboksToggleName, createUnleashContext(user), false)
    }

    suspend fun situasjonEnabled(user: AuthenticatedUser): Boolean = withContext(Dispatchers.IO) {
        unleashClient.isEnabled(situasjonToggleName, createUnleashContext(user), false)
    }

    private fun createUnleashContext(user: AuthenticatedUser): UnleashContext {
        return UnleashContext.builder()
            .userId(user.ident)
            .build()
    }

}
