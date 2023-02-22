package no.nav.personbruker.dittnav.api.config

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.install
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respond
import mu.KotlinLogging

private val log = KotlinLogging.logger ( "secureLog" )

internal fun Application.installStatusPages() {
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            when (cause) {
                is CookieNotSetException -> {
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

                else -> {
                    call.respond(HttpStatusCode.ServiceUnavailable)
                    log.warn("${cause.message}", cause.stackTrace)
                }
            }
        }
    }
}

suspend fun ApplicationCall.respondServiceUnavailable(message: String, cause: ConsumeException) {
    respond(HttpStatusCode.ServiceUnavailable)
    log.warn("$message. ${cause.message}", cause.details,cause)
}

open class ConsumeException(message: String, cause: Throwable) : Exception(message, cause) {
    val details = "Caused by ${cause::class} with original error message: ${cause.message}"
}

class ConsumeSakerException(message: String, cause: Throwable) : ConsumeException(message, cause)

class ConsumePersonaliaException(message: String, cause: Throwable) : ConsumeException(message, cause)

class ConsumeEventException(message: String, cause: Throwable) : ConsumeException(message, cause)

class ProduceEventException(message: String, cause: Throwable) : ConsumeException(message, cause)