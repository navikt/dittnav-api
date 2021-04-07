package no.nav.personbruker.dittnav.api.brukernotifikasjon

import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import no.nav.personbruker.dittnav.api.common.respondWithError
import no.nav.personbruker.dittnav.api.config.authenticatedUser
import no.nav.personbruker.dittnav.api.legacy.logWhenTokenIsAboutToExpire
import org.slf4j.LoggerFactory

fun Route.brukernotifikasjoner(service: BrukernotifikasjonService) {

    val log = LoggerFactory.getLogger(BrukernotifikasjonService::class.java)

    get("/brukernotifikasjon/count") {
        log.logWhenTokenIsAboutToExpire(authenticatedUser)
        try {
            val totalNumberOfEvents = service.totalNumberOfEvents(authenticatedUser)
            call.respond(HttpStatusCode.OK, totalNumberOfEvents)

        } catch(exception: Exception) {
            respondWithError(call, log, exception)
        }
    }

    get("/brukernotifikasjon/count/inactive") {
        log.logWhenTokenIsAboutToExpire(authenticatedUser)
        try {
            val numberOfInactiveEvents = service.numberOfInactive(authenticatedUser)
            call.respond(HttpStatusCode.OK, numberOfInactiveEvents)

        } catch(exception: Exception) {
            respondWithError(call, log, exception)
        }
    }

    get("/brukernotifikasjon/count/active") {
        log.logWhenTokenIsAboutToExpire(authenticatedUser)
        try {
            val numberOfActiveEvents = service.numberOfActive(authenticatedUser)
            call.respond(HttpStatusCode.OK, numberOfActiveEvents)

        } catch(exception: Exception) {
            respondWithError(call, log, exception)
        }
    }

}
