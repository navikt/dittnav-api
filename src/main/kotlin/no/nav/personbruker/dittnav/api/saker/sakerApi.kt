package no.nav.personbruker.dittnav.api.saker

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import no.nav.personbruker.dittnav.api.config.authenticatedUser

fun Route.saker(
    consumer: MineSakerConsumer
) {
    get("/saker") {
            val result = consumer.hentSistEndredeSakstemaer(authenticatedUser)
            call.respond(HttpStatusCode.OK, result)
    }
}
