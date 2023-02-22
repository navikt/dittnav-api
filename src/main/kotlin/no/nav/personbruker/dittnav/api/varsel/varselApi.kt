package no.nav.personbruker.dittnav.api.varsel

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import kotlinx.serialization.Serializable
import no.nav.personbruker.dittnav.api.beskjed.BeskjedMergerService
import no.nav.personbruker.dittnav.api.config.authenticatedUser
import no.nav.personbruker.dittnav.api.innboks.InnboksConsumer
import no.nav.personbruker.dittnav.api.oppgave.OppgaveConsumer

fun Route.varsel(beskjedService: BeskjedMergerService, oppgaveService: OppgaveConsumer, innboksService: InnboksConsumer) {

    get("/varsel/antall") {
        val beskjedEvents = beskjedService.getActiveEvents(authenticatedUser)
        val oppgaveEvents = oppgaveService.getActiveOppgaver(authenticatedUser)
        val innboksEvents = innboksService.getActiveInnboksEvents(authenticatedUser)

        call.respond(
            HttpStatusCode.OK, AntallVarsler(
                beskjeder = beskjedEvents.results().size,
                oppgaver = oppgaveEvents.size,
                innbokser = innboksEvents.size
            )
        )
    }
}

@Serializable
private data class AntallVarsler(val beskjeder: Int, val oppgaver: Int, val innbokser: Int)
