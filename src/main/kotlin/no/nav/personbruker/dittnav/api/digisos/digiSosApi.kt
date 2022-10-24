package no.nav.personbruker.dittnav.api.digisos


import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import mu.KotlinLogging
import no.nav.personbruker.dittnav.api.config.authenticatedUser

fun Route.digiSos(
    service: DigiSosService
) {

    post("/digisos/paabegynte/done") {
        val doneDto = call.receive<DoneDTO>()
        service.markEventAsDone(authenticatedUser, doneDto)
        call.respond(HttpStatusCode.OK)
    }
}
