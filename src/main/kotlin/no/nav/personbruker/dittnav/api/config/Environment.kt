package no.nav.personbruker.dittnav.api.config

import org.slf4j.LoggerFactory
import java.net.URL

data class Environment(
        val dittNAVLegacyURL: URL = URL(getEnvVar("LEGACY_API_URL")),
        val dittNAVEventsURL: URL = URL(getEnvVar("EVENT_HANDLER_URL")),
        val corsAllowedOrigins: String = getEnvVar("CORS_ALLOWED_ORIGINS")
)

private val log = LoggerFactory.getLogger(Environment::class.java)

fun getEnvVar(varName: String): String {
    val varValue = System.getenv(varName)
    log.info("Read the environment variable $varName to be $varValue")
    return varValue ?: throw IllegalArgumentException("Variable $varName cannot be empty")

}
