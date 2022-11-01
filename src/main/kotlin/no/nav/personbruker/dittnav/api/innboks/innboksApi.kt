package no.nav.personbruker.dittnav.api.innboks

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import no.nav.personbruker.dittnav.api.config.authenticatedUser

fun Route.innboks(innboksConsumer: InnboksConsumer) {

    get("/innboks") {
        val innboksEvents = innboksConsumer.getActiveInnboksEvents(authenticatedUser)
        call.respond(HttpStatusCode.OK, innboksEvents)
    }

    get("/innboks/inaktiv") {
        val innboksEvents = innboksConsumer.getInactiveInnboksEvents(authenticatedUser)
        call.respond(HttpStatusCode.OK, innboksEvents)
    }
}
