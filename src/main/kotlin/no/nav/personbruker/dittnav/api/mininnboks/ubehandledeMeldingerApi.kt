package no.nav.personbruker.dittnav.api.mininnboks

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import no.nav.personbruker.dittnav.api.config.authenticatedUser
import org.slf4j.LoggerFactory

fun Route.ubehandledeMeldingerApi() {

    get("/meldinger/ubehandlede") {
        call.respond(emptyList<Unit>())
    }
}
