package no.nav.personbruker.dittnav.api.common

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import org.slf4j.Logger

suspend fun respondWithError(call: ApplicationCall, log: Logger, exception: Exception) {
    when(exception) {
        is ConsumeEventException -> {
            val feilkode = HttpStatusCode.ServiceUnavailable
            call.respond(feilkode)
            log.warn("Klarte ikke å hente eventer. Returnerer feilkoden '$feilkode' til frontend. $exception", exception)
        }
        is ConsumeSakerException -> {
            val feilkode = HttpStatusCode.ServiceUnavailable
            call.respond(feilkode)
            log.warn("Klarte ikke å hente saker. Returnerer feilkoden '$feilkode' til frontend. $exception", exception)
        }
        is ProduceEventException -> {
            val feilkode = HttpStatusCode.ServiceUnavailable
            call.respond(feilkode)
            log.warn("Klarte ikke å produsere done-event. Returnerer feilkoden '$feilkode' til frontend. $exception", exception)
        }
        else -> {
            val feilkode = HttpStatusCode.InternalServerError
            call.respond(feilkode)
            log.error("Ukjent feil oppstod. Returnerer feilkoden '$feilkode' til frontend", exception)
        }
    }
}
