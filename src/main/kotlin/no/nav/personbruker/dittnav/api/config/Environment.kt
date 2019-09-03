package no.nav.personbruker.dittnav.api.config

import java.net.URL

data class Environment(val securityAudience: String = getEnvVar("AUDIENCE", "dummyAudience"),
                       val securityJwksIssuer: String = getEnvVar("JWKS_ISSUER", "dummyIssuer"),
                       val securityJwksUri: URL = URL(getEnvVar("JWKS_URI", "https://dummyUrl.com")),
                       val dittNAVLegacyURL: String = getDittNAVLegacyUrl(),
                       val dittNAVEventsURL: String = getDittNAVEventsUrl()
)

fun getEnvVar(varName: String, defaultValue: String? = null): String {
    return System.getenv(varName) ?: defaultValue
    ?: throw IllegalArgumentException("Variable $varName cannot be empty")
}

private fun getDittNAVLegacyUrl(): String {
    return when (System.getenv("NAIS_CLUSTER_NAME")) {
        "dev-sbs" -> "https://dittnav-legacy-api-q0.nais.oera-q.local/person/dittnav-legacy-api/"
        "prod-sbs" -> "https://dittnav-legacy-api.nais.oera.no/person/dittnav-legacy-api/"
        else -> "https://dummyURL.com"
    }
}

private fun getDittNAVEventsUrl(): String {
    return when (System.getenv("NAIS_CLUSTER_NAME")) {
        "dev-sbs" -> "https://dittnav-event-handler.dev-sbs.nais.io/"
        "prod-sbs" -> "https://dittnav-event-handler.prod-sbs.nais.io"
        else -> "https://dummyURL.com"
    }
}
