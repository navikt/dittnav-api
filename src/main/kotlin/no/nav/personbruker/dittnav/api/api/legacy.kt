package no.nav.personbruker.dittnav.api.api

import io.ktor.application.call
import io.ktor.auth.parseAuthorizationHeader
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import no.nav.personbruker.dittnav.api.legacy.LegacyConsumer

fun Route.legacyMeldinger(legacyConsumer: LegacyConsumer) {
    get("/meldinger/ubehandlede") {
        val authHeader = call.request.parseAuthorizationHeader()?.render()
        val ubehandledeMeldinger = legacyConsumer.checkHeaderAndGetLegacyContent(
            "meldinger/ubehandlede", authHeader)
        call.respond(ubehandledeMeldinger.first, ubehandledeMeldinger.second)
    }
}

fun Route.legacyPabegynte(legacyConsumer: LegacyConsumer) {
    get("/saker/paabegynte") {
        val authHeader = call.request.parseAuthorizationHeader()?.render()
        val paabegynte = legacyConsumer.checkHeaderAndGetLegacyContent(
            "saker/paabegynte", authHeader)
        call.respond(paabegynte.first, paabegynte.second)
    }
}

fun Route.legacyPersoninfo(legacyConsumer: LegacyConsumer) {
    get("/person/personinfo") {
        val authHeader = call.request.parseAuthorizationHeader()?.render()
        val personinfo = legacyConsumer.checkHeaderAndGetLegacyContent(
            "person/personinfo", authHeader)
        call.respond(personinfo.first, personinfo.second)
    }
}

