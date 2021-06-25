package no.nav.personbruker.dittnav.api.digisos

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import no.nav.personbruker.dittnav.api.common.respondWithError
import no.nav.personbruker.dittnav.api.config.authenticatedUser
import no.nav.personbruker.dittnav.api.config.executeOnUnexpiredTokensOnly
import org.slf4j.LoggerFactory

fun Route.digiSos(
    service: DigiSosService
) {

    val log = LoggerFactory.getLogger(DigiSosService::class.java)

    get("/digisos/paabegynte") {
        executeOnUnexpiredTokensOnly {
            try {
                val result = service.getActiveEvents(authenticatedUser)
                if(result.hasErrors()) {
                    log.warn("En eller flere kilder feilet: ${result.failedSources()}")
                }
                call.respond(result.determineHttpCode(), result.results())

            } catch (exception: Exception) {
                respondWithError(call, log, exception)
            }
        }
    }

    get("/digisos/paabegynte/inaktiv") {
        executeOnUnexpiredTokensOnly {
            try {
                val result = service.getInactiveEvents(authenticatedUser)
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
