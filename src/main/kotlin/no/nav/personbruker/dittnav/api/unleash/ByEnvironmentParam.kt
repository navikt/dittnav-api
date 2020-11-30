package no.nav.personbruker.dittnav.api.unleash

import no.finn.unleash.strategy.Strategy

class ByEnvironmentParam(private val environment: String): Strategy {
    override fun getName() = "byEnvironmentParam"

    override fun isEnabled(parameters: MutableMap<String, String>): Boolean {
        return parameters["environment"] == environment
    }

}