package no.nav.personbruker.dittnav.api.unleash

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import no.finn.unleash.Unleash
import no.finn.unleash.UnleashContext
import no.nav.personbruker.dittnav.api.authentication.AuthenticatedUser


class UnleashService(private val unleashClient: Unleash) {

    companion object {
        const val digisosPaabegynteToggleName: String = "digisosPaabegynteEnabled"
    }

    suspend fun digiSosPaabegynteEnabled(user: AuthenticatedUser): Boolean = withContext(Dispatchers.IO) {
        unleashClient.isEnabled(digisosPaabegynteToggleName, createUnleashContext(user), false)
    }

    private fun createUnleashContext(user: AuthenticatedUser): UnleashContext {
        return UnleashContext.builder()
            .userId(user.ident)
            .build()
    }

}
