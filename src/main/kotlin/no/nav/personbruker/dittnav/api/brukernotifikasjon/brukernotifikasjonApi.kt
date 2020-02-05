package no.nav.personbruker.dittnav.api.brukernotifikasjon

import io.ktor.application.call
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import no.nav.personbruker.dittnav.api.common.hentInnloggetBruker

fun Route.brukernotifikasjoner(brukernotifikasjonService: BrukernotifikasjonService) {

    get("/brukernotifikasjoner") {
        call.respond(brukernotifikasjonService.getBrukernotifikasjoner(hentInnloggetBruker()))
    }
}
