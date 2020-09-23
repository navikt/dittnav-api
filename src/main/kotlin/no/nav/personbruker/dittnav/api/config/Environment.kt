package no.nav.personbruker.dittnav.api.config

import java.net.URL

data class Environment(
        val legacyApiURL: URL = URL(getEnvVar("LEGACY_API_URL").trimEnd('/')),
        val eventHandlerURL: URL = URL(getEnvVar("EVENT_HANDLER_URL").trimEnd('/')),
        val corsAllowedOrigins: String = getEnvVar("CORS_ALLOWED_ORIGINS"),
        val corsAllowedSchemes: String = getOptionalEnvVar("CORS_ALLOWED_SCHEMES", "https"),
        val includeVarselinnboks: Boolean = getOptionalEnvVar("INCLUDE_VARSELINNBOKS", "false").toBoolean(),
        val isRunningInDev: Boolean = isRunningInDev()
)

fun getEnvVar(varName: String): String {
    return System.getenv(varName)
            ?: throw IllegalArgumentException("Appen kan ikke starte uten av miljøvariabelen $varName er satt.")
}

fun getOptionalEnvVar(varName: String, defaultValue: String): String {
    return System.getenv(varName) ?: return defaultValue
}

private fun isRunningInDev(clusterName: String? = System.getenv("NAIS_CLUSTER_NAME")): Boolean {
    var runningInDev = true
    if (clusterName != null && clusterName == "prod-sbs") {
        runningInDev = false
    }
    return runningInDev
}
