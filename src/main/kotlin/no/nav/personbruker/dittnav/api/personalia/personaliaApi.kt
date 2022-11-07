package no.nav.personbruker.dittnav.api.personalia

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import mu.KotlinLogging
import no.nav.personbruker.dittnav.api.common.respondWithError
import no.nav.personbruker.dittnav.api.config.authenticatedUser

fun Route.personalia(
    service: PersonaliaService
) {
    val log = KotlinLogging.logger { }

    get("/navn") {
        try {
            log.info {"Henter navn fra pdl" }
            val result = service.hentNavn(authenticatedUser)
            call.respond(HttpStatusCode.OK, result)

        } catch (exception: Exception) {
            log.warn { "Klarte ikke å hente navn fra pdl" }
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
