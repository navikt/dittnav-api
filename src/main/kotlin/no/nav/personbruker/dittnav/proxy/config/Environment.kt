package no.nav.personbruker.dittnav.proxy.config

import java.net.URL

data class Environment(val securityAudience: String = getEnvVar("AUDIENCE", "dummyAudience"),
                       val securityJwksIssuer: String = getEnvVar("JWKS_ISSUER", "dummyIssuer"),
                       val securityJwksUri: URL = URL(getEnvVar("JWKS_URI", "https://dummyUrl.com"))
)

fun getEnvVar(varName: String, defaultValue: String? = null): String {
    return System.getenv(varName) ?: defaultValue
    ?: throw IllegalArgumentException("Variable $varName cannot be empty")
}
