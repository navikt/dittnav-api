package no.nav.personbruker.dittnav.api.beskjed

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import no.nav.personbruker.dittnav.api.common.respondWithError
import no.nav.personbruker.dittnav.api.config.authenticatedUser
import no.nav.personbruker.dittnav.api.config.executeOnUnexpiredTokensOnly
import org.slf4j.LoggerFactory

fun Route.beskjed(
    beskjedVarselSwitcher: BeskjedVarselSwitcher
) {

    val log = LoggerFactory.getLogger(BeskjedService::class.java)

    get("/beskjed") {
        executeOnUnexpiredTokensOnly {
            try {
                val result = beskjedVarselSwitcher.getActiveEvents(authenticatedUser)
                if(result.hasErrors()) {
                    log.warn("En eller flere kilder feilet: ${result.failedSources()}")
                }
                call.respond(result.determineHttpCode(), result.results())

            } catch (exception: Exception) {
                respondWithError(call, log, exception)
            }
        }
    }

    get("/beskjed/inaktiv") {
        executeOnUnexpiredTokensOnly {
            try {
                val result = beskjedVarselSwitcher.getInactiveEvents(authenticatedUser)
                if(result.hasErrors()) {
                    log.warn("En eller flere kilder feilet: ${result.failedSources()}")
                }
                call.respond(result.determineHttpCode(), result.results())

            } catch (exception: Exception) {
                respondWithError(call, log, exception)
            }
        }
    }

}
