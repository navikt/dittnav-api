package no.nav.personbruker.dittnav.api.beskjed

import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import mu.KotlinLogging
import no.nav.personbruker.dittnav.api.config.authenticatedUser

fun Route.beskjed(
    beskjedMergerService: BeskjedMergerService
) {

    val log = KotlinLogging.logger { }

    get("/beskjed") {
        val result = beskjedMergerService.getActiveEvents(authenticatedUser)
        if (result.hasErrors()) {
            log.warn("En eller flere kilder feilet: ${result.failedSources()}")
        }
        call.respond(result.determineHttpCode(), result.results())
    }

    get("/beskjed/inaktiv") {
        val result = beskjedMergerService.getInactiveEvents(authenticatedUser)
        if (result.hasErrors()) {
            log.warn("En eller flere kilder feilet: ${result.failedSources()}")
        }
        call.respond(result.determineHttpCode(), result.results())
    }

}
