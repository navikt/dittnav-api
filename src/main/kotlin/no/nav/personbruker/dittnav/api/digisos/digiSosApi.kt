package no.nav.personbruker.dittnav.api.digisos

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import mu.KotlinLogging
import no.nav.personbruker.dittnav.api.common.respondWithError
import no.nav.personbruker.dittnav.api.config.authenticatedUser

fun Route.digiSos(
    service: DigiSosService
) {

    val log = KotlinLogging.logger { }

    post("/digisos/paabegynte/done") {
        try {
            val doneDto = call.receive<DoneDTO>()
            service.markEventAsDone(authenticatedUser, doneDto)
            call.respond(HttpStatusCode.OK)

        } catch (exception: Exception) {
            respondWithError(call, log, exception)
        }
    }
}
