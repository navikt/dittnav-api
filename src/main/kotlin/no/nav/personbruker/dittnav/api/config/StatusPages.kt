package no.nav.personbruker.dittnav.api.config

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.install
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respond
import mu.KotlinLogging
import no.nav.personbruker.dittnav.api.common.ConnectionFailedException

private val log = KotlinLogging.logger {  }

internal fun Application.installStatusPages() {
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            when (cause) {
                is CookieNotSetException -> {
                    log.info("401: fant ikke selvbetjening-idtoken")
                    call.respond(HttpStatusCode.Unauthorized)
                }
                is ConsumeEventException ->
                    call.respondServiceUnavailable("Klarte ikke å hente eventer", cause)
                is ConsumeSakerException ->
                    call.respondServiceUnavailable("Klarte ikke å hente saker", cause)
                is ConsumePersonaliaException ->
                    call.respondServiceUnavailable("Klarte ikke å hente personalia", cause)
                is ProduceEventException ->
                    call.respondServiceUnavailable("Kunne ikke markere varsel som lest", cause)
                is ConnectionFailedException ->
                    call.respondServiceUnavailable(cause.message?: "ConnectionFailedException",cause)
                else ->
                    call.respondServiceUnavailable("Ukjent feil",cause)
            }
        }
    }
}

suspend fun ApplicationCall.respondServiceUnavailable(message: String, cause: Throwable) {
    respond(HttpStatusCode.ServiceUnavailable)
    log.warn("$message. $cause", cause)
}

class ConsumeSakerException(message: String, cause: Throwable) : Exception(message, cause)

class ConsumePersonaliaException(message: String, cause: Throwable) : Exception(message, cause)

class ConsumeEventException(message: String, cause: Throwable) : Exception(message, cause)

class ProduceEventException(message: String, cause: Throwable) : Exception(message, cause)