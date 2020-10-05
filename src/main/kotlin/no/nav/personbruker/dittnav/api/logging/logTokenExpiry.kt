package no.nav.personbruker.dittnav.api.logging

import io.ktor.application.ApplicationCall
import io.ktor.response.ResponseHeaders
import org.slf4j.Logger

fun logTokenExpiry(log: Logger, call: ApplicationCall) {
    val headers: ResponseHeaders = call.response.headers

    if (headers.contains("x-token-expires-soon")) {
        log.info("Et kall fra dittnav er gjort med et token som har to minutter igjen.")
    }
}

