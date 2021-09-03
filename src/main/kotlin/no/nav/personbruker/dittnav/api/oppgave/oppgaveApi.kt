package no.nav.personbruker.dittnav.api.oppgave

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import no.nav.personbruker.dittnav.api.common.respondWithError
import no.nav.personbruker.dittnav.api.config.authenticatedUser
import no.nav.personbruker.dittnav.api.config.executeOnUnexpiredTokensOnly
import org.slf4j.LoggerFactory

fun Route.oppgave(oppgaveService: OppgaveService) {

    val log = LoggerFactory.getLogger(OppgaveService::class.java)

    get("/oppgave") {
        executeOnUnexpiredTokensOnly {
            try {
                val result = oppgaveService.getActiveOppgaveEvents(authenticatedUser)
                if(result.hasErrors()) {
                    log.warn("En eller flere kilder feilet: ${result.failedSources()}")
                }
                call.respond(HttpStatusCode.OK, result)

            } catch(exception: Exception) {
                respondWithError(call, log, exception)
            }
        }
    }

    get("/oppgave/inaktiv") {
        executeOnUnexpiredTokensOnly {
            try {
                val result = oppgaveService.getInactiveOppgaveEvents(authenticatedUser)
                if(result.hasErrors()) {
                    log.warn("En eller flere kilder feilet: ${result.failedSources()}")
                }
                call.respond(HttpStatusCode.OK, result)

            } catch(exception: Exception) {
                respondWithError(call, log, exception)
            }
        }
    }

}
