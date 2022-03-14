package no.nav.personbruker.dittnav.api.mininnboks

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import no.nav.personbruker.dittnav.api.config.authenticatedUser
import no.nav.personbruker.dittnav.api.legacy.LegacyApiOperations
import no.nav.personbruker.dittnav.api.legacy.log

fun Route.ubehandledeMeldingerApi(ubehandledeMeldingerService: UbehandledeMeldingerService) {

    get(LegacyApiOperations.UBEHANDLEDE_MELDINGER.path) {
        try {
            val oppfolgingInfo = ubehandledeMeldingerService.getUbehandledeMeldinger(authenticatedUser)

            call.respond(oppfolgingInfo)
        } catch (e: Exception) {
            log.warn("Det skjedde en feil mot mininnboks. Feilmelding: [${e.message}]. $authenticatedUser", e)
            call.respond(HttpStatusCode.InternalServerError)
        }
    }
}
