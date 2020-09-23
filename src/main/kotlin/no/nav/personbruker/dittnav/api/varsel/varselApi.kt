package no.nav.personbruker.dittnav.api.varsel

import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import no.nav.personbruker.dittnav.api.common.respondWithError
import no.nav.personbruker.dittnav.api.config.innloggetBruker
import org.slf4j.LoggerFactory

fun Route.varsel(varselService: VarselService) {

    val log = LoggerFactory.getLogger(VarselService::class.java)

    get("/varsel") {
        try {
            val varselEvents = varselService.getActiveVarselEvents(innloggetBruker)
            call.respond(HttpStatusCode.OK, varselEvents)
        } catch(exception: Exception) {
            respondWithError(call, log, exception)
        }
    }

    get("/varsel/inaktiv") {
        try {
            val varselEvents = varselService.getInactiveVarselEvents(innloggetBruker)
            call.respond(HttpStatusCode.OK, varselEvents)
        } catch(exception: Exception) {
            respondWithError(call, log, exception)
        }
    }
}
