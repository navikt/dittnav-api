package no.nav.personbruker.dittnav.api.config

import no.nav.personbruker.dittnav.common.util.config.BooleanEnvVar.getEnvVarAsBoolean
import no.nav.personbruker.dittnav.common.util.config.StringEnvVar.getEnvVar
import no.nav.personbruker.dittnav.common.util.config.StringEnvVar.getEnvVarAsList
import no.nav.personbruker.dittnav.common.util.config.UrlEnvVar.getEnvVarAsURL
import java.net.URL

data class Environment(
    val eventHandlerURL: URL = getEnvVarAsURL("EVENT_HANDLER_URL", trimTrailingSlash = true),
    val corsAllowedOrigins: String = getEnvVar("CORS_ALLOWED_ORIGINS"),
    val corsAllowedSchemes: String = getEnvVar("CORS_ALLOWED_SCHEMES", "https"),
    val corsAllowedHeaders: List<String> = getEnvVarAsList("CORS_ALLOWED_HEADERS"),
    val fakeUnleashIncludeVarsel: Boolean = getEnvVarAsBoolean("FAKE_UNLEASH_INCLUDE_VARSEL", false),
    val fakeUnleashIncludeDigiSos: Boolean = getEnvVarAsBoolean("FAKE_UNLEASH_INCLUDE_DIGISOS", false),
    val unleashApiUrl: String = getEnvVar("UNLEASH_API_URL"),
    val digiSosSoknadBaseURL: URL = getEnvVarAsURL("DIGISOS_API_URL", trimTrailingSlash = true),
    val digiSosInnsynBaseURL: URL = getEnvVarAsURL("DIGISOS_INNSYN_API_URL", trimTrailingSlash = true),
    val mineSakerURL: URL = getEnvVarAsURL("MINE_SAKER_URL", trimTrailingSlash = true),
    val sakerApiUrl: URL = getEnvVarAsURL("MINE_SAKER_API_URL", trimTrailingSlash = true),
    val mineSakerApiClientId: String = getEnvVar("MINE_SAKER_API_CLIENT_ID"),
    val personaliaApiUrl: URL = getEnvVarAsURL("PERSONALIA_API_URL"),
    val personaliaApiClientId: String = getEnvVar("PERSONALIA_API_CLIENT_ID"),
    val eventhandlerClientId: String = getEnvVar("EVENTHANDLER_CLIENT_ID"),
    val meldekortApiUrl: URL = getEnvVarAsURL("MELDEKORT_API_URL"),
    val oppfolgingApiUrl: URL = getEnvVarAsURL("OPPFOLGING_API_URL"),
    val innloggingsinfoUrl: URL = getEnvVarAsURL("INNLOGGINGSINFO_URL"),
    val isRunningInDev: Boolean = isRunningInDev()
)

private fun isRunningInDev(clusterName: String? = System.getenv("NAIS_CLUSTER_NAME")): Boolean {
    var runningInDev = true
    if (clusterName != null && clusterName == "prod-sbs") {
        runningInDev = false
    }
    return runningInDev
}
