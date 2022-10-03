package no.nav.personbruker.dittnav.api.meldekort

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import mu.KotlinLogging
import no.nav.personbruker.dittnav.api.config.authenticatedUser

fun Route.meldekortApi(meldekortService: MeldekortService) {

    val log = KotlinLogging.logger { }

    get("/meldekortinfo") {
        try {
            val meldekortInfo = meldekortService.getMeldekortInfo(authenticatedUser)

            call.respond(meldekortInfo)
        } catch (e: Exception) {
            log.warn("Det skjedde en feil mot meldekort. Feilmelding: [${e.message}]. $authenticatedUser", e)
            call.respond(HttpStatusCode.InternalServerError)
        }
    }

    get("/meldekortstatus") {
        try {
            val meldekortStatus = meldekortService.getMeldekortStatus(authenticatedUser)

            call.respond(meldekortStatus)
        } catch (e: Exception) {
            log.warn("Det skjedde en feil mot meldekort. Feilmelding: [${e.message}]. $authenticatedUser", e)
            call.respond(HttpStatusCode.InternalServerError)
        }
    }
}
