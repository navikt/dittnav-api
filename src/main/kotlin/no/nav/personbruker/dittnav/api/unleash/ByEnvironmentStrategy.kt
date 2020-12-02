package no.nav.personbruker.dittnav.api.unleash

import no.finn.unleash.strategy.Strategy

class ByEnvironmentStrategy(private val environment: String): Strategy {
    override fun getName() = "byEnvironment"

    override fun isEnabled(parameters: MutableMap<String, String>): Boolean {
        return parameters["miljø"]
                ?.split(",")
                ?.contains(environment)
                ?: false
    }

}