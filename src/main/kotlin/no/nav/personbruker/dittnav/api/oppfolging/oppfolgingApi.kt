package no.nav.personbruker.dittnav.api.oppfolging


import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import mu.KotlinLogging
import no.nav.personbruker.dittnav.api.config.authenticatedUser

fun Route.oppfolgingApi(oppfolgingService: OppfolgingService) {

    val log = KotlinLogging.logger { }

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
