package no.nav.personbruker.dittnav.api.varsel


import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import kotlinx.serialization.Serializable
import mu.KotlinLogging
import no.nav.personbruker.dittnav.api.beskjed.BeskjedMergerService
import no.nav.personbruker.dittnav.api.common.respondWithError
import no.nav.personbruker.dittnav.api.config.authenticatedUser
import no.nav.personbruker.dittnav.api.innboks.InnboksService
import no.nav.personbruker.dittnav.api.oppgave.OppgaveService

fun Route.varsel(beskjedService: BeskjedMergerService, oppgaveService: OppgaveService, innboksService: InnboksService) {

    val log = KotlinLogging.logger { }

    get("/varsel/antall") {
        try {
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
        } catch (exception: Exception) {
            respondWithError(call, log, exception)
        }
    }
}

@Serializable
private data class AntallVarsler(val beskjeder: Int, val oppgaver: Int, val innbokser: Int)
