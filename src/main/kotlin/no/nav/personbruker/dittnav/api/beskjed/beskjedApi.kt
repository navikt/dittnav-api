package no.nav.personbruker.dittnav.api.beskjed

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import no.nav.personbruker.dittnav.api.common.respondWithError
import no.nav.personbruker.dittnav.api.config.innloggetBruker
import no.nav.personbruker.dittnav.api.config.isRunningInDev
import org.slf4j.LoggerFactory

fun Route.beskjed(beskjedService: BeskjedService, mergeBeskjedMedVarselService: MergeBeskjedMedVarselService) {

    val log = LoggerFactory.getLogger(BeskjedService::class.java)

    get("/beskjed") {
        try {
            val beskjedEvents = beskjedService.getActiveBeskjedEvents(innloggetBruker)
            call.respond(HttpStatusCode.OK, beskjedEvents)

        } catch (exception: Exception) {
            respondWithError(call, log, exception)
        }
    }

    get("/beskjed/inaktiv") {
        try {
            val beskjedEvents = beskjedService.getInactiveBeskjedEvents(innloggetBruker)
            call.respond(HttpStatusCode.OK, beskjedEvents)

        } catch (exception: Exception) {
            respondWithError(call, log, exception)
        }
    }

    if (isRunningInDev()) {
        log.info("Kjører i et dev-miljø, aktiverer grensesnittet for vasler sammen med beskjeder.")

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
