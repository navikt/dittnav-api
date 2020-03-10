package no.nav.personbruker.dittnav.api.oppgave

import io.ktor.application.call
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import no.nav.personbruker.dittnav.api.config.innloggetBruker

fun Route.oppgave(oppgaveService: OppgaveService) {

    get("/oppgave") {
        val oppgaveEvents = oppgaveService.getActiveOppgaveEvents(innloggetBruker)
        call.respond(oppgaveEvents)
    }

    get("/oppgave/inaktiv") {
        val oppgaveEvents = oppgaveService.getInactiveOppgaveEvents(innloggetBruker)
        call.respond(oppgaveEvents)
    }
}
