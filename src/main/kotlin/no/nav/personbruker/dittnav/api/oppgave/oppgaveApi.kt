package no.nav.personbruker.dittnav.api.oppgave

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import mu.KotlinLogging
import no.nav.personbruker.dittnav.api.common.respondWithError
import no.nav.personbruker.dittnav.api.config.authenticatedUser

fun Route.oppgave(oppgaveService: OppgaveService) {

    val log = KotlinLogging.logger {  }

    get("/oppgave") {
        try {
            val oppgaveEvents = oppgaveService.getActiveOppgaver(authenticatedUser)
            call.respond(HttpStatusCode.OK, oppgaveEvents)
        } catch (exception: Exception) {
            respondWithError(call, log, exception)
        }
    }

    get("/oppgave/inaktiv") {
        try {
            val oppgaveEvents = oppgaveService.getInactiveOppgaver(authenticatedUser)
            call.respond(HttpStatusCode.OK, oppgaveEvents)
        } catch (exception: Exception) {
            respondWithError(call, log, exception)
        }
    }

}
