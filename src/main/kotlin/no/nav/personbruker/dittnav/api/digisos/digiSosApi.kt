package no.nav.personbruker.dittnav.api.digisos


import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
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
