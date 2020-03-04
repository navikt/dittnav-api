package no.nav.personbruker.dittnav.api.innboks

import io.ktor.application.call
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import no.nav.personbruker.dittnav.api.common.innloggetBruker

fun Route.innboks(innboksService: InnboksService) {

    get("/innboks") {
        val innboksEventsAsBrukernotifikasjoner = innboksService.getInnboksEventsAsBrukernotifikasjoner(innloggetBruker)
        call.respond(innboksEventsAsBrukernotifikasjoner)
    }
}
