package no.nav.personbruker.dittnav.api.saksoversikt

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import no.nav.personbruker.dittnav.api.config.authenticatedUser
import no.nav.personbruker.dittnav.api.legacy.LegacyApiOperations
import no.nav.personbruker.dittnav.api.legacy.log

fun Route.saksoversiktApi(saksoversiktService: SaksoversiktService) {

    get(LegacyApiOperations.SAKSTEMA.path) {
        try {
            val sakstema = saksoversiktService.getSakstema(authenticatedUser)

            call.respond(sakstema)
        } catch (e: Exception) {
            log.warn("Det skjedde en feil mot saksoverikt. Feilmelding: [${e.message}]. $authenticatedUser", e)
            call.respond(HttpStatusCode.InternalServerError)
        }
    }

    get(LegacyApiOperations.PAABEGYNTESAKER.path) {
        try {
            val oppfolging = saksoversiktService.getPaabegynte(authenticatedUser)

            call.respond(oppfolging)
        } catch (e: Exception) {
            log.warn("Det skjedde en feil mot saksoverikt. Feilmelding: [${e.message}]. $authenticatedUser", e)
            call.respond(HttpStatusCode.InternalServerError)
        }
    }
}
