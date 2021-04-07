package no.nav.personbruker.dittnav.api.oppgave

import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import no.nav.personbruker.dittnav.api.common.respondWithError
import no.nav.personbruker.dittnav.api.config.authenticatedUser
import no.nav.personbruker.dittnav.api.legacy.logWhenTokenIsAboutToExpire
import org.slf4j.LoggerFactory

fun Route.oppgave(oppgaveService: OppgaveService) {

    val log = LoggerFactory.getLogger(OppgaveService::class.java)

    get("/oppgave") {
        log.logWhenTokenIsAboutToExpire(authenticatedUser)
        try {
            val oppgaveEvents = oppgaveService.getActiveOppgaveEvents(authenticatedUser)
            call.respond(HttpStatusCode.OK, oppgaveEvents)
        } catch(exception: Exception) {
            respondWithError(call, log, exception)
        }
    }

    get("/oppgave/inaktiv") {
        log.logWhenTokenIsAboutToExpire(authenticatedUser)
        try {
            val oppgaveEvents = oppgaveService.getInactiveOppgaveEvents(authenticatedUser)
            call.respond(HttpStatusCode.OK, oppgaveEvents)
        } catch(exception: Exception) {
            respondWithError(call, log, exception)
        }
    }
}
