package no.nav.personbruker.dittnav.api.brukernotifikasjon

import io.ktor.application.call
import io.ktor.auth.parseAuthorizationHeader
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import no.nav.personbruker.dittnav.api.common.extractTokenFromRequest

fun Route.brukernotifikasjoner(brukernotifikasjonService: BrukernotifikasjonService) {

    get("/person/dittnav-api/brukernotifikasjoner") {
        val token = extractTokenFromRequest()
        call.respond(brukernotifikasjonService.getBrukernotifikasjoner(token))
    }
}
