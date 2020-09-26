package no.nav.personbruker.dittnav.api.brukernotifikasjon

import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import no.nav.personbruker.dittnav.api.common.respondWithError
import no.nav.personbruker.dittnav.api.config.innloggetBruker
import org.slf4j.LoggerFactory

fun Route.brukernotifikasjoner(service: BrukernotifikasjonService) {

    val log = LoggerFactory.getLogger(BrukernotifikasjonService::class.java)

    get("/brukernotifikasjon/count") {
        try {
            val totalNumberOfEvents = service.totalNumberOfEvents(innloggetBruker)
            call.respond(HttpStatusCode.OK, totalNumberOfEvents)

        } catch(exception: Exception) {
            respondWithError(call, log, exception)
        }
    }

    get("/brukernotifikasjon/count/active") {
        try {
            val numberOfActiveEvents = service.numberOfActive(innloggetBruker)
            call.respond(HttpStatusCode.OK, numberOfActiveEvents)

        } catch(exception: Exception) {
            respondWithError(call, log, exception)
        }
    }

}
