package no.nav.personbruker.dittnav.api.unleash

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import no.nav.personbruker.dittnav.api.common.respondWithError
import no.nav.personbruker.dittnav.api.config.authenticatedUser
import no.nav.personbruker.dittnav.api.config.executeOnUnexpiredTokensOnly
import org.slf4j.LoggerFactory

fun Route.unleash(
        unleashService: UnleashService
) {
    val log = LoggerFactory.getLogger(unleashService::class.java)

    get("/unleash/innboks") {
        executeOnUnexpiredTokensOnly {
            try {
                val result = unleashService.innboksEnabled(authenticatedUser)
                call.respond(HttpStatusCode.OK, result)

            } catch (exception: Exception) {
                respondWithError(call, log, exception)
            }
        }
    }

}