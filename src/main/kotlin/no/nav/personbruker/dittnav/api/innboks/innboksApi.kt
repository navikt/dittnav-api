package no.nav.personbruker.dittnav.api.innboks

import io.ktor.application.call
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import no.nav.personbruker.dittnav.api.config.innloggetBruker

fun Route.innboks(innboksService: InnboksService) {

    get("/innboks") {
        val innboksEvents = innboksService.getActiveInnboksEvents(innloggetBruker)
        call.respond(innboksEvents)
    }

    get("/innboks/inaktiv") {
        val innboksEvents = innboksService.getInactiveInnboksEvents(innloggetBruker)
        call.respond(innboksEvents)
    }
}
