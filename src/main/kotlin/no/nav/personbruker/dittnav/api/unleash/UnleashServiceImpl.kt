package no.nav.personbruker.dittnav.api.unleash

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import no.finn.unleash.DefaultUnleash
import no.finn.unleash.Unleash
import no.finn.unleash.UnleashContext
import no.finn.unleash.util.UnleashConfig
import no.nav.personbruker.dittnav.common.security.AuthenticatedUser

class UnleashServiceImpl(isRunningInDev: Boolean, unleashUrl: String): UnleashService {

    private val unleashClient: Unleash

    init {
        val appName = "dittnav-api"

        val envContext = if (isRunningInDev) {
            "dev"
        } else {
            "prod"
        }

        val config = UnleashConfig.builder()
                .appName(appName)
                .environment(envContext)
                .unleashAPI(unleashUrl)
                .build()

        unleashClient = DefaultUnleash(config)
    }

    override suspend fun mergeVarselEnabled(user: AuthenticatedUser): Boolean = withContext(Dispatchers.IO) {
        unleashClient.isEnabled("mergeVarselEnabled", createUnleashContext(user), false)
    }

    private fun createUnleashContext(user: AuthenticatedUser): UnleashContext {
        return UnleashContext.builder()
                .userId(user.ident)
                .build()
    }
}