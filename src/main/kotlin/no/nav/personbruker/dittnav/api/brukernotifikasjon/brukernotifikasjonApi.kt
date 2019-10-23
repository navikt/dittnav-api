package no.nav.personbruker.dittnav.api.brukernotifikasjon

import io.ktor.application.call
import io.ktor.auth.parseAuthorizationHeader
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get

fun Route.brukernotifikasjoner(brukernotifikasjonService: BrukernotifikasjonService) {

    get("/person/dittnav-api/brukernotifikasjoner") {
        val authHeader = call.request.parseAuthorizationHeader()
        call.respond(brukernotifikasjonService.getBrukernotifikasjoner(authHeader))
    }
}
