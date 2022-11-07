package no.nav.personbruker.dittnav.api.common


import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respond
import mu.KLogger


suspend fun respondWithError(call: ApplicationCall, log: KLogger, exception: Exception) {
    when (exception) {
        is ConsumeEventException -> {
            val feilkode = HttpStatusCode.ServiceUnavailable
            call.respond(feilkode)
            log.warn(
                "Klarte ikke å hente eventer. Returnerer feilkoden '$feilkode' til frontend. $exception",
                exception
            )
        }

        is ConsumeSakerException -> {
            val feilkode = HttpStatusCode.ServiceUnavailable
            call.respond(feilkode)
            log.warn("Klarte ikke å hente saker. Returnerer feilkoden '$feilkode' til frontend. $exception", exception)
        }

        is ProduceEventException -> {
            val feilkode = HttpStatusCode.ServiceUnavailable
            call.respond(feilkode)
            log.warn(
                "Klarte ikke å produsere done-event. Returnerer feilkoden '$feilkode' til frontend. $exception",
                exception
            )
        }

        is QueryResponseException -> {
            val feilkode = HttpStatusCode.ServiceUnavailable
            call.respond(feilkode)
            log.warn(
                "Klarte ikke å hente navn. Returnerer feilkoden $feilkode. $exception",
                exception
            )
        }

        is QueryRequestException -> {
            val feilkode = HttpStatusCode.ServiceUnavailable
            call.respond(feilkode)
            log.warn(
                "Klarte ikke å hente navn. Returnerer feilkoden $feilkode. $exception",
                exception
            )
        }

        is TransformationException -> {
            val feilkode = HttpStatusCode.InternalServerError
            call.respond(feilkode)
            log.warn(
                "Mottok verdi som ikke kunne konverteres til den interne-modellen. Returnerer feilkoden $feilkode. $exception",
                exception
            )
        }

        else -> {
            val feilkode = HttpStatusCode.InternalServerError
            call.respond(feilkode)
            log.error("Ukjent feil oppstod. Returnerer feilkoden '$feilkode' til frontend", exception)
        }
    }
}

class ConsumeSakerException(message: String, cause: Throwable) : Exception(message,cause)

class ConsumeEventException(message: String, cause: Throwable) : Exception(message, cause)

class ProduceEventException(message: String, cause: Throwable) : Exception(message, cause)

class QueryRequestException(message: String, cause: Throwable) : Exception(message, cause)

class QueryResponseException(message: String) : Exception(message)

class TransformationException(message: String) : Exception(message)
