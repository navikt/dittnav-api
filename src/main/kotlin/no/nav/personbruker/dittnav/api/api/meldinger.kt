package no.nav.personbruker.dittnav.api.api

import io.ktor.application.call
import io.ktor.auth.parseAuthorizationHeader
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import no.nav.personbruker.dittnav.api.config.Environment
import no.nav.personbruker.dittnav.api.meldinger.MeldingService

val meldingService = MeldingService()

fun Route.meldinger(environment: Environment) {
    get("/events") {
        val authHeader = call.request.parseAuthorizationHeader()
        if (authHeader != null) {
            call.respond(HttpStatusCode.OK, meldingService.getMeldinger(environment, authHeader))
        }
    }
}
