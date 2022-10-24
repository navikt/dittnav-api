package no.nav.personbruker.dittnav.api.oppgave


import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import no.nav.personbruker.dittnav.api.config.authenticatedUser

fun Route.oppgave(oppgaveService: OppgaveService) {

    get("/oppgave") {
        val oppgaveEvents = oppgaveService.getActiveOppgaver(authenticatedUser)
        call.respond(HttpStatusCode.OK, oppgaveEvents)
    }

    get("/oppgave/inaktiv") {
        val oppgaveEvents = oppgaveService.getInactiveOppgaver(authenticatedUser)
        call.respond(HttpStatusCode.OK, oppgaveEvents)
    }

}
