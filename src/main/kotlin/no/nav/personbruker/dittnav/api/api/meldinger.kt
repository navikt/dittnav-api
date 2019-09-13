package no.nav.personbruker.dittnav.api.api

import io.ktor.application.call
import io.ktor.auth.parseAuthorizationHeader
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import no.nav.personbruker.dittnav.api.meldinger.MeldingService

fun Route.meldinger(meldingService: MeldingService) {
    get("/events") {
        val authHeader = call.request.parseAuthorizationHeader()
        if (authHeader != null) {
            call.respond(HttpStatusCode.OK, meldingService.getMeldinger(authHeader))
        }
    }
}
