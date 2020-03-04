package no.nav.personbruker.dittnav.api.config

import org.slf4j.LoggerFactory
import java.net.URL

data class Environment(
        val legacyApiURL: URL = URL(getEnvVar("LEGACY_API_URL").trimEnd('/')),
        val eventHandlerURL: URL = URL(getEnvVar("EVENT_HANDLER_URL").trimEnd('/')),
        val corsAllowedOrigins: String = getEnvVar("CORS_ALLOWED_ORIGINS")
)

private val log = LoggerFactory.getLogger(Environment::class.java)

fun getEnvVar(varName: String): String {
    val varValue = System.getenv(varName)
    return varValue ?: throw IllegalArgumentException("Variable $varName cannot be empty")

}
