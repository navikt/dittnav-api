package no.nav.personbruker.dittnav.api.config

data class Environment(val dittNAVLegacyURL: String = getDittNAVLegacyUrl(),
                       val dittNAVEventsURL: String = getDittNAVEventsUrl()
)

fun getEnvVar(varName: String, defaultValue: String? = null): String {
    return System.getenv(varName) ?: defaultValue
    ?: throw IllegalArgumentException("Variable $varName cannot be empty")
}

private fun getDittNAVLegacyUrl(): String {
    return when (currentClusterName()) {
        "dev-sbs" -> "https://dittnav-legacy-api-q0.nais.oera-q.local/person/dittnav-legacy-api/"
        "prod-sbs" -> "https://dittnav-legacy-api.nais.oera.no/person/dittnav-legacy-api/"
        else -> "http://localhost:8090/person/dittnav-legacy-api/"
    }
}

private fun getDittNAVEventsUrl(): String {
    return when (currentClusterName()) {
        "dev-sbs" -> "https://dittnav-event-handler.dev-sbs.nais.io/"
        "prod-sbs" -> "https://dittnav-event-handler.prod-sbs.nais.io/"
        else -> "http://localhost:8092/"
    }
}

private fun currentClusterName() = System.getenv("NAIS_CLUSTER_NAME")
