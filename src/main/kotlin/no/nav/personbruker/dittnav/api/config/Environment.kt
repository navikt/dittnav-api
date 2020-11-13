package no.nav.personbruker.dittnav.api.config

import no.nav.personbruker.dittnav.common.util.config.BooleanEnvVar.getEnvVarAsBoolean
import no.nav.personbruker.dittnav.common.util.config.StringEnvVar.getEnvVar
import no.nav.personbruker.dittnav.common.util.config.UrlEnvVar.getEnvVarAsURL
import java.net.URL

data class Environment(
        val legacyApiURL: URL = getEnvVarAsURL("LEGACY_API_URL", trimTrailingSlash = true),
        val eventHandlerURL: URL = getEnvVarAsURL("EVENT_HANDLER_URL", trimTrailingSlash = true),
        val innloggingsstatusUrl: URL = getEnvVarAsURL("INNLOGGINGSSTATUS_URL", trimTrailingSlash = true),
        val corsAllowedOrigins: String = getEnvVar("CORS_ALLOWED_ORIGINS"),
        val corsAllowedSchemes: String = getEnvVar("CORS_ALLOWED_SCHEMES", "https"),
        val includeVarselinnboks: Boolean = getEnvVarAsBoolean("INCLUDE_VARSELINNBOKS", false),
        val isRunningInDev: Boolean = isRunningInDev()
)

private fun isRunningInDev(clusterName: String? = System.getenv("NAIS_CLUSTER_NAME")): Boolean {
    var runningInDev = true
    if (clusterName != null && clusterName == "prod-sbs") {
        runningInDev = false
    }
    return runningInDev
}
