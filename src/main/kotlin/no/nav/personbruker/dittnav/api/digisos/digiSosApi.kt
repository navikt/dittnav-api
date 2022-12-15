package no.nav.personbruker.dittnav.api.digisos


import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import no.nav.personbruker.dittnav.api.config.authenticatedUser

fun Route.digiSos(
    consumer: DigiSosConsumer
) {

    post("/digisos/paabegynte/done") {
        val doneDto = call.receive<DoneDTO>()
        consumer.markEventAsDone(authenticatedUser, doneDto)
        call.respond(HttpStatusCode.OK)
    }

    get("/digisos/utkast") {
        val utkast = consumer.getPaabegynt(authenticatedUser).map { it.toUtkast() }

        call.respond(utkast)
    }

    get("/digisos/utkast/antall") {
        val paabegynte = consumer.getPaabegynt(authenticatedUser)

        call.respond(Antall(paabegynte.size))
    }
}

@Serializable
private data class Antall(val antall: Int)
