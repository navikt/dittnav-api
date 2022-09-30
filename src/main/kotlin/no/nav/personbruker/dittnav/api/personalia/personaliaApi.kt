package no.nav.personbruker.dittnav.api.personalia

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import no.nav.personbruker.dittnav.api.common.respondWithError
import no.nav.personbruker.dittnav.api.config.authenticatedUser
import org.slf4j.LoggerFactory

fun Route.personalia(
    service: PersonaliaService
) {
    val log = LoggerFactory.getLogger(PersonaliaService::class.java)

    get("/navn") {
        try {
            val result = service.hentNavn(authenticatedUser)
            call.respond(HttpStatusCode.OK, result)

        } catch (exception: Exception) {
            respondWithError(call, log, exception)
        }

    }

    get("/ident") {
        try {
            val result = service.hentIdent(authenticatedUser)
            call.respond(HttpStatusCode.OK, result)

        } catch (exception: Exception) {
            respondWithError(call, log, exception)
        }

    }


}
