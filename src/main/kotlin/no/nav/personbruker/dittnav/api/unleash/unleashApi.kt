package no.nav.personbruker.dittnav.api.unleash

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import mu.KotlinLogging
import no.nav.personbruker.dittnav.api.common.respondWithError
import no.nav.personbruker.dittnav.api.config.authenticatedUser


fun Route.unleash(
    unleashService: UnleashService
) {
    val log = KotlinLogging.logger {  }

    get("/unleash/situasjon") {
        try {
            val result = unleashService.situasjonEnabled(authenticatedUser)
            call.respond(HttpStatusCode.OK, result)

        } catch (exception: Exception) {
            respondWithError(call, log, exception)
        }
    }

    get("/unleash/minside") {
        try {
            val result = unleashService.minSideEnabled(authenticatedUser)
            call.respond(HttpStatusCode.OK, result)

        } catch (exception: Exception) {
            respondWithError(call, log, exception)
        }
    }
}
