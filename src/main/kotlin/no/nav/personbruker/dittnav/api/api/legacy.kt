package no.nav.personbruker.dittnav.api.api

import io.ktor.application.call
import io.ktor.auth.parseAuthorizationHeader
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import no.nav.personbruker.dittnav.api.LegacyConsumer
import no.nav.personbruker.dittnav.api.config.Environment

fun Route.legacyMeldinger(environment: Environment) {
    get("/meldinger/ubehandlede") {
        val authHeader = call.request.parseAuthorizationHeader()?.render()
        val ubehandledeMeldinger = LegacyConsumer().checkHeaderAndGetLegacyContent(
            "meldinger/ubehandlede", environment, authHeader)
        call.respond(ubehandledeMeldinger.first, ubehandledeMeldinger.second)
    }
}

fun Route.legacyPabegynte(environment: Environment) {
    get("/saker/paabegynte") {
        val authHeader = call.request.parseAuthorizationHeader()?.render()
        val paabegynte = LegacyConsumer().checkHeaderAndGetLegacyContent(
            "saker/paabegynte", environment, authHeader)
        call.respond(paabegynte.first, paabegynte.second)
    }
}

fun Route.legacyPersoninfo(environment: Environment) {
    get("/person/personinfo") {
        val authHeader = call.request.parseAuthorizationHeader()?.render()
        val personinfo = LegacyConsumer().checkHeaderAndGetLegacyContent(
            "person/personinfo", environment, authHeader)
        call.respond(personinfo.first, personinfo.second)
    }
}

