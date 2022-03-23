package no.nav.personbruker.dittnav.api.mininnboks

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import no.nav.personbruker.dittnav.api.config.authenticatedUser
import org.slf4j.LoggerFactory

fun Route.ubehandledeMeldingerApi(ubehandledeMeldingerService: UbehandledeMeldingerService) {

    val log = LoggerFactory.getLogger("ubehandledeMeldingerApi")

    get("/meldinger/ubehandlede") {
        try {
            val oppfolgingInfo = ubehandledeMeldingerService.getUbehandledeMeldinger(authenticatedUser)

            call.respond(oppfolgingInfo)
        } catch (e: Exception) {
            log.warn("Det skjedde en feil mot mininnboks. Feilmelding: [${e.message}]. $authenticatedUser", e)
            call.respond(HttpStatusCode.InternalServerError)
        }
    }
}
