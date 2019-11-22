package no.nav.personbruker.dittnav.api.config

import java.net.URL

data class Environment(val dittNAVLegacyURL: URL = URL(getEnvVar("LEGACY-API_URL")),
                       val dittNAVEventsURL: URL = URL(getEnvVar("EVENT-HANDLER_URL"))
)

fun getEnvVar(varName: String): String {
    return System.getenv(varName) ?: throw IllegalArgumentException("Variable $varName cannot be empty")

}
