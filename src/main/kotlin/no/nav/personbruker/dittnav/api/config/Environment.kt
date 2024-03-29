package no.nav.personbruker.dittnav.api.config

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.url
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import no.nav.personbruker.dittnav.common.util.config.BooleanEnvVar.getEnvVarAsBoolean
import no.nav.personbruker.dittnav.common.util.config.StringEnvVar.getEnvVar
import no.nav.personbruker.dittnav.common.util.config.StringEnvVar.getEnvVarAsList
import no.nav.personbruker.dittnav.common.util.config.UrlEnvVar.getEnvVarAsURL
import java.net.URL


data class Environment(
    val eventHandlerURL: URL = getEnvVarAsURL("EVENT_HANDLER_URL", trimTrailingSlash = true),
    val eventAggregatorURL: URL = getEnvVarAsURL("EVENT_AGGREGATOR_URL", trimTrailingSlash = true),
    val corsAllowedOrigins: String = getEnvVar("CORS_ALLOWED_ORIGINS"),
    val corsAllowedSchemes: String = getEnvVar("CORS_ALLOWED_SCHEMES", "https"),
    val corsAllowedHeaders: List<String> = getEnvVarAsList("CORS_ALLOWED_HEADERS"),
    val fakeUnleashIncludeDigiSos: Boolean = getEnvVarAsBoolean("FAKE_UNLEASH_INCLUDE_DIGISOS", false),
    val unleashApiUrl: String = getEnvVar("UNLEASH_API_URL"),
    val digiSosSoknadBaseURL: URL = getEnvVarAsURL("DIGISOS_API_URL", trimTrailingSlash = true),
    val digiSosClientId: String = getEnvVar("DIGISOS_API_CLIENT_ID"),
    val mineSakerURL: URL = getEnvVarAsURL("MINE_SAKER_URL", trimTrailingSlash = true),
    val mineSakerApiUrl: URL = getEnvVarAsURL("MINE_SAKER_API_URL", trimTrailingSlash = true),
    val mineSakerApiClientId: String = getEnvVar("MINE_SAKER_API_CLIENT_ID"),
    val personaliaApiUrl: URL = getEnvVarAsURL("PERSONALIA_API_URL"),
    val personaliaApiClientId: String = getEnvVar("PERSONALIA_API_CLIENT_ID"),
    val eventhandlerClientId: String = getEnvVar("EVENTHANDLER_CLIENT_ID"),
    val eventaggregatorClientId: String = getEnvVar("EVENTAGGREGATOR_CLIENT_ID"),
    val oppfolgingApiUrl: URL = getEnvVarAsURL("OPPFOLGING_API_URL"),
    val loginservicDiscoveryUrl: String = getEnvVar("LOGINSERVICE_IDPORTEN_DISCOVERY_URL"),
    val loginserviceIdportenAudience: String = getEnvVar("LOGINSERVICE_IDPORTEN_AUDIENCE")

)

@Serializable
data class LoginserviceMetadata(val jwks_uri: String, val issuer: String) {
    companion object {
        fun get(httpClient: HttpClient, discoveryUrl: String): LoginserviceMetadata = runBlocking {
            httpClient.get { url(discoveryUrl) }.body()
        }
    }
}

object NaisEnvironment {

    fun currentClusterName() : String? = System.getenv("NAIS_CLUSTER_NAME")

    fun isRunningInProd(): Boolean {
        val clusterName = currentClusterName()
        return clusterName == "prod-sbs" || clusterName == "prod-gcp"
    }

    fun isRunningInDev(): Boolean {
        return !isRunningInProd()
    }

}
