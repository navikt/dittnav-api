package no.nav.personbruker.dittnav.api.personalia


import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import no.nav.personbruker.dittnav.api.config.authenticatedUser


fun Route.personalia(
    service: PersonaliaService
) {

    get("/navn") {
        val result = service.hentNavn(authenticatedUser)
        call.respond(HttpStatusCode.OK, result)
    }

    get("/ident") {
        val result = service.hentIdent(authenticatedUser)
        call.respond(HttpStatusCode.OK, result)
    }


}
