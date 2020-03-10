package no.nav.personbruker.dittnav.api.beskjed

import io.ktor.application.call
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import no.nav.personbruker.dittnav.api.config.innloggetBruker

fun Route.beskjed(beskjedService: BeskjedService) {

    get("/beskjed") {
        val beskjedEvents = beskjedService.getActiveBeskjedEvents(innloggetBruker)
        call.respond(beskjedEvents)
    }

    get("/beskjed/inaktiv") {
        val beskjedEvents = beskjedService.getInactiveBeskjedEvents(innloggetBruker)
        call.respond(beskjedEvents)
    }
}
