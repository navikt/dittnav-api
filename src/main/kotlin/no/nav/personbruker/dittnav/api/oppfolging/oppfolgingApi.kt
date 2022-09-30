package no.nav.personbruker.dittnav.api.oppfolging

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import no.nav.personbruker.dittnav.api.config.authenticatedUser
import org.slf4j.LoggerFactory

fun Route.oppfolgingApi(oppfolgingService: OppfolgingService) {

    val log = LoggerFactory.getLogger("oppfolgingApi")

    get("/oppfolging") {
        try {
            val oppfolgingInfo = oppfolgingService.getOppfolging(authenticatedUser)

            call.respond(oppfolgingInfo)
        } catch (e: Exception) {
            log.warn("Det skjedde en feil mot oppfølging. Feilmelding: [${e.message}]. $authenticatedUser", e)
            call.respond(HttpStatusCode.InternalServerError)
        }
    }
}
