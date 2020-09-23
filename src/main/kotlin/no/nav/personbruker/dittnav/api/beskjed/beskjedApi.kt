package no.nav.personbruker.dittnav.api.beskjed

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import no.nav.personbruker.dittnav.api.common.respondWithError
import no.nav.personbruker.dittnav.api.config.Environment
import no.nav.personbruker.dittnav.api.config.innloggetBruker
import org.slf4j.LoggerFactory

fun Route.beskjed(
    mergeBeskjedMedVarselService: MergeBeskjedMedVarselService,
    beskjedVarselSwitcher: BeskjedVarselSwitcher,
    environment: Environment
) {

    val log = LoggerFactory.getLogger(BeskjedService::class.java)

    get("/beskjed") {
        try {
            val beskjedEvents = beskjedVarselSwitcher.getActiveEvents(innloggetBruker)
            call.respond(HttpStatusCode.OK, beskjedEvents)

        } catch (exception: Exception) {
            respondWithError(call, log, exception)
        }
    }

    get("/beskjed/inaktiv") {
        try {
            val beskjedEvents = beskjedVarselSwitcher.getInactiveEvents(innloggetBruker)
            call.respond(HttpStatusCode.OK, beskjedEvents)

        } catch (exception: Exception) {
            respondWithError(call, log, exception)
        }
    }

    if (environment.isRunningInDev) {
        log.info("Kjører i et dev-miljø, aktiverer grensesnittet for varsler sammen med beskjeder.")

        get("/beskjed/merged") {
            try {
                val events = mergeBeskjedMedVarselService.getActiveEvents(innloggetBruker)
                call.respond(HttpStatusCode.OK, events)

            } catch (exception: Exception) {
                respondWithError(call, log, exception)
            }
        }

        get("/beskjed/merged/inaktiv") {
            try {
                val events = mergeBeskjedMedVarselService.getInactiveEvents(innloggetBruker)
                call.respond(HttpStatusCode.OK, events)

            } catch (exception: Exception) {
                respondWithError(call, log, exception)
            }
        }
    }

}
