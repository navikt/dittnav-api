package no.nav.personbruker.dittnav.api.saker

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import no.nav.personbruker.dittnav.api.common.respondWithError
import no.nav.personbruker.dittnav.api.config.authenticatedUser
import no.nav.personbruker.dittnav.api.config.executeOnUnexpiredTokensOnly
import no.nav.personbruker.dittnav.api.digisos.DigiSosService
import org.slf4j.LoggerFactory

fun Route.saker(
        service: SakerService
) {
    val log = LoggerFactory.getLogger(SakerService::class.java)

    get("/saker") {
        executeOnUnexpiredTokensOnly {
            try {
                val result = service.getSaker(authenticatedUser)
                call.respond(HttpStatusCode.OK, result)

            } catch (exception: Exception) {
                respondWithError(call, log, exception)
            }
        }
    }

}
