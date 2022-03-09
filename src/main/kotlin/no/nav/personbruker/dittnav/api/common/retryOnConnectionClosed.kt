package no.nav.personbruker.dittnav.api.common

import org.apache.http.ConnectionClosedException

inline fun <reified T> retryOnConnectionClosed(retries: Int = 3, outgoingCall: () -> T): T {
    var attempts = 0

    lateinit var lastError: Exception

    while (attempts < retries) {
        try {
            return outgoingCall()
        } catch (e: ConnectionClosedException) {
            attempts++
            lastError = e
        }
    }

    throw ConnectionFailedException("Klarte ikke hente data etter $attempts forsøk. Viser info for siste feil.", lastError)
}

class ConnectionFailedException(message: String, cause: Exception): Exception(message, cause)
