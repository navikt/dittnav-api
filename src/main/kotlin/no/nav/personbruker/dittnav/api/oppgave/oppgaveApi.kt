package no.nav.personbruker.dittnav.api.oppgave


import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import mu.KotlinLogging
import no.nav.personbruker.dittnav.api.common.respondWithError
import no.nav.personbruker.dittnav.api.config.authenticatedUser

fun Route.oppgave(oppgaveService: OppgaveService) {

    val log = KotlinLogging.logger { }

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
