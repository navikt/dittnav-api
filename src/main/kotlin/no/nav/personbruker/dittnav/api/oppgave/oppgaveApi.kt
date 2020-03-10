package no.nav.personbruker.dittnav.api.oppgave

import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import no.nav.personbruker.dittnav.api.common.ExceptionResponseWrapper.respondWithError
import no.nav.personbruker.dittnav.api.config.innloggetBruker
import org.slf4j.LoggerFactory

fun Route.oppgave(oppgaveService: OppgaveService) {

    val log = LoggerFactory.getLogger(OppgaveService::class.java)

    get("/oppgave") {
        try {
            val oppgaveEvents = oppgaveService.getActiveOppgaveEvents(innloggetBruker)
            call.respond(HttpStatusCode.OK, oppgaveEvents)
        } catch(exception: Exception) {
            respondWithError(call, log, exception)
        }
    }

    get("/oppgave/inaktiv") {
        try {
            val oppgaveEvents = oppgaveService.getInactiveOppgaveEvents(innloggetBruker)
            call.respond(HttpStatusCode.OK, oppgaveEvents)
        } catch(exception: Exception) {
            respondWithError(call, log, exception)
        }
    }
}
