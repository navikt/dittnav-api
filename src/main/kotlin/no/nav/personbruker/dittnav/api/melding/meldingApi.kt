package no.nav.personbruker.dittnav.api.melding

import io.ktor.application.call
import io.ktor.auth.parseAuthorizationHeader
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get

fun Route.meldinger(meldingService: MeldingService) {
    get("/person/dittnav-api/meldinger") {
        val authHeader = call.request.parseAuthorizationHeader()
        call.respond(meldingService.getMeldinger(authHeader))
    }
}
