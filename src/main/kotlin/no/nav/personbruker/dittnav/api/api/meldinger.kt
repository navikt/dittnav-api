package no.nav.personbruker.dittnav.api.api

import io.ktor.application.call
import io.ktor.auth.parseAuthorizationHeader
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import no.nav.personbruker.dittnav.api.meldinger.MeldingService

fun Route.meldinger(meldingService: MeldingService) {
    get("/meldinger") {
        val authHeader = call.request.parseAuthorizationHeader()
        call.respond(meldingService.getMeldinger(authHeader))
    }
}
