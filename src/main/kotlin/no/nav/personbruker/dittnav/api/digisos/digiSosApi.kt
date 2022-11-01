package no.nav.personbruker.dittnav.api.digisos


import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import no.nav.personbruker.dittnav.api.config.authenticatedUser

fun Route.digiSos(
    consumer: DigiSosConsumer
) {

    post("/digisos/paabegynte/done") {
        val doneDto = call.receive<DoneDTO>()
        consumer.markEventAsDone(authenticatedUser, doneDto)
        call.respond(HttpStatusCode.OK)
    }
}
