package no.nav.personbruker.dittnav.api.beskjed

import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import mu.KotlinLogging
import no.nav.personbruker.dittnav.api.common.respondWithError
import no.nav.personbruker.dittnav.api.config.authenticatedUser

fun Route.beskjed(
    beskjedMergerService: BeskjedMergerService
) {

    val log = KotlinLogging.logger { }

    get("/beskjed") {
        try {
            val result = beskjedMergerService.getActiveEvents(authenticatedUser)
            if (result.hasErrors()) {
                log.warn("En eller flere kilder feilet: ${result.failedSources()}")
            }
            call.respond(result.determineHttpCode(), result.results())

        } catch (exception: Exception) {
            respondWithError(call, log, exception)
        }

    }

    get("/beskjed/inaktiv") {

        try {
            val result = beskjedMergerService.getInactiveEvents(authenticatedUser)
            if (result.hasErrors()) {
                log.warn("En eller flere kilder feilet: ${result.failedSources()}")
            }
            call.respond(result.determineHttpCode(), result.results())

        } catch (exception: Exception) {
            respondWithError(call, log, exception)
        }

    }

}
