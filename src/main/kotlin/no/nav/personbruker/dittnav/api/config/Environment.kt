package no.nav.personbruker.dittnav.api.config

import java.net.URL

data class Environment(val dittNAVLegacyURL: URL = URL(getEnvVar("LEGACY-API_URL", "http://localhost:8090/person/dittnav-legacy-api")),
                       val dittNAVEventsURL: URL = URL(getEnvVar("EVENT-HANDLER_URL", "http://localhost:8092"))
)

fun getEnvVar(varName: String, defaultValue: String? = null): String {
    return System.getenv(varName) ?: defaultValue
    ?: throw IllegalArgumentException("Variable $varName cannot be empty")
}
