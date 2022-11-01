package no.nav.personbruker.dittnav.api.saker

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import mu.KotlinLogging
import no.nav.personbruker.dittnav.api.common.respondWithError
import no.nav.personbruker.dittnav.api.config.authenticatedUser

fun Route.saker(
    service: SakerService
) {
    val log = KotlinLogging.logger { }

    get("/saker") {
        try {
            val result = service.hentSistEndredeSakstemaer(authenticatedUser)
            call.respond(HttpStatusCode.OK, result)

        } catch (exception: Exception) {
            respondWithError(call, log, exception)
        }
    }

}
