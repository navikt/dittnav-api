package no.nav.personbruker.dittnav.api.saker

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import mu.KotlinLogging
import no.nav.personbruker.dittnav.api.common.respondWithError
import no.nav.personbruker.dittnav.api.config.authenticatedUser

fun Route.saker(
    service: SakerService
) {
    val log = KotlinLogging.logger {  }

    get("/saker") {
            try {
                val result = service.hentSisteToEndredeSakstemaer(authenticatedUser)
                call.respond(HttpStatusCode.OK, result)

            } catch (exception: Exception) {
                respondWithError(call, log, exception)
            }
    }

}
