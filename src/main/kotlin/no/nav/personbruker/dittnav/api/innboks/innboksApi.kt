package no.nav.personbruker.dittnav.api.innboks

import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import mu.KotlinLogging
import no.nav.personbruker.dittnav.api.common.respondWithError
import no.nav.personbruker.dittnav.api.config.authenticatedUser

fun Route.innboks(innboksService: InnboksService) {

    val log = KotlinLogging.logger { }

    get("/innboks") {
        try {
            val innboksEvents = innboksService.getActiveInnboksEvents(authenticatedUser)
            call.respond(HttpStatusCode.OK, innboksEvents)
        } catch (exception: Exception) {
            respondWithError(call, log, exception)
        }
    }

    get("/innboks/inaktiv") {
        try {
            val innboksEvents = innboksService.getInactiveInnboksEvents(authenticatedUser)
            call.respond(HttpStatusCode.OK, innboksEvents)
        } catch (exception: Exception) {
            respondWithError(call, log, exception)
        }
    }
}
