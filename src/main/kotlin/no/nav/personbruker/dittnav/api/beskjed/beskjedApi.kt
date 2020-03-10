package no.nav.personbruker.dittnav.api.beskjed

import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import no.nav.personbruker.dittnav.api.common.ExceptionResponseWrapper.respondWithError
import no.nav.personbruker.dittnav.api.config.innloggetBruker
import org.slf4j.LoggerFactory

fun Route.beskjed(beskjedService: BeskjedService) {

    val log = LoggerFactory.getLogger(BeskjedService::class.java)

    get("/beskjed") {
        try {
            val beskjedEvents = beskjedService.getActiveBeskjedEvents(innloggetBruker)
            call.respond(HttpStatusCode.OK, beskjedEvents)
        } catch(exception: Exception) {
            respondWithError(call, log, exception)
        }
    }

    get("/beskjed/inaktiv") {
        try {
            val beskjedEvents = beskjedService.getInactiveBeskjedEvents(innloggetBruker)
            call.respond(HttpStatusCode.OK, beskjedEvents)
        } catch(exception: Exception) {
            respondWithError(call, log, exception)
        }
    }
}
