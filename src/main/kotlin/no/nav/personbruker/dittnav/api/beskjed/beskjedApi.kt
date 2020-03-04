package no.nav.personbruker.dittnav.api.beskjed

import io.ktor.application.call
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import no.nav.personbruker.dittnav.api.common.innloggetBruker

fun Route.beskjed(beskjedService: BeskjedService) {

    get("/beskjed") {
        val beskjedEventsAsBrukernotifikasjoner = beskjedService.getBeskjedEventsAsBrukernotifikasjoner(innloggetBruker)
        call.respond(beskjedEventsAsBrukernotifikasjoner)
    }
}
