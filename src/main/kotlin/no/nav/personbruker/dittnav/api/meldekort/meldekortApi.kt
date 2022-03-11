package no.nav.personbruker.dittnav.api.meldekort

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import no.nav.personbruker.dittnav.api.config.authenticatedUser
import no.nav.personbruker.dittnav.api.legacy.LegacyApiOperations
import no.nav.personbruker.dittnav.api.legacy.log

fun Route.meldekortApi(meldekortService: MeldekortService) {

    get(LegacyApiOperations.MELDEKORT.path) {
        try {
            val meldekortInfo = meldekortService.getMeldekortInfo(authenticatedUser)

            call.respond(meldekortInfo)
        } catch (e: Exception) {
            log.warn("Det skjedde en feil mot meldekort. Feilmelding: [${e.message}]. $authenticatedUser", e)
            call.respond(HttpStatusCode.InternalServerError)
        }
    }
}
