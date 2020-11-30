package no.nav.personbruker.dittnav.api.unleash

import no.finn.unleash.strategy.Strategy

class ByApplicationStrategy(private val appName: String) : Strategy {

    override fun getName() = "byApplication"

    override fun isEnabled(parameters: MutableMap<String, String>): Boolean {
        return parameters["appName"] == appName
    }
}