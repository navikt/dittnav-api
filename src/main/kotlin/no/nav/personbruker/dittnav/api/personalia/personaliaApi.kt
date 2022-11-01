package no.nav.personbruker.dittnav.api.personalia


import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import no.nav.personbruker.dittnav.api.config.authenticatedUser


fun Route.personalia(
    consumer: PersonaliaConsumer
) {

    get("/navn") {
        val result = consumer.hentNavn(authenticatedUser)
        call.respond(HttpStatusCode.OK, result)
    }

    get("/ident") {
        val result = consumer.hentIdent(authenticatedUser)
        call.respond(HttpStatusCode.OK, result)
    }

}
