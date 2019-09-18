package no.nav.personbruker.dittnav.api.legacy

import io.ktor.application.call
import io.ktor.auth.parseAuthorizationHeader
import io.ktor.client.response.readBytes
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import no.nav.personbruker.dittnav.api.legacy.LegacyConsumer

fun Route.legacyMeldinger(legacyConsumer: LegacyConsumer) {
    get("/meldinger/ubehandlede") {
        val authHeader = call.request.parseAuthorizationHeader()
        val ubehandledeMeldinger = legacyConsumer.getLegacyContent(
            "meldinger/ubehandlede", authHeader)
        call.respond(ubehandledeMeldinger.status, ubehandledeMeldinger.readBytes())
        }

}

fun Route.legacyPabegynte(legacyConsumer: LegacyConsumer) {
    get("/saker/paabegynte") {
        val authHeader = call.request.parseAuthorizationHeader()
        val paabegynte = legacyConsumer.getLegacyContent(
            "saker/paabegynte", authHeader)
        call.respond(paabegynte.status, paabegynte.readBytes())
    }
}

fun Route.legacyPersoninfo(legacyConsumer: LegacyConsumer) {
    get("/person/personinfo") {
        val authHeader = call.request.parseAuthorizationHeader()
        val personinfo = legacyConsumer.getLegacyContent(
            "person/personinfo", authHeader)
        call.respond(personinfo.status, personinfo.readBytes())
    }
}

