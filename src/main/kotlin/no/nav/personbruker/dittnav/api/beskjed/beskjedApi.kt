package no.nav.personbruker.dittnav.api.beskjed

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import no.nav.personbruker.dittnav.api.common.respondWithError
import no.nav.personbruker.dittnav.api.config.Environment
import no.nav.personbruker.dittnav.api.config.innloggetBruker
import org.slf4j.LoggerFactory

fun Route.beskjed(
    beskjedVarselSwitcher: BeskjedVarselSwitcher
) {

    val log = LoggerFactory.getLogger(BeskjedService::class.java)

    get("/beskjed") {
        try {
            val result = beskjedVarselSwitcher.getActiveEvents(innloggetBruker)
            if(result.hasErrors()) {
                log.warn("En eller flere kilder feilet: ${result.errors()}")
            }
            call.respond(result.determineHttpCode(), result.results())

        } catch (exception: Exception) {
            respondWithError(call, log, exception)
        }
    }

    get("/beskjed/inaktiv") {
        try {
            val result = beskjedVarselSwitcher.getInactiveEvents(innloggetBruker)
            if(result.hasErrors()) {
                log.warn("En eller flere kilder feilet: ${result.errors()}")
            }
            call.respond(result.determineHttpCode(), result.results())

        } catch (exception: Exception) {
            respondWithError(call, log, exception)
        }
    }

}
