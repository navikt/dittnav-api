package no.nav.personbruker.dittnav.api.melding

import io.ktor.application.call
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import no.nav.personbruker.dittnav.api.common.extractTokenFromRequest

fun Route.meldinger(meldingService: MeldingService) {
    get("/person/dittnav-api/meldinger") {
        val token = extractTokenFromRequest()
        val meldinger = meldingService.getMeldinger(token)
        call.respond(meldinger)
    }
}
