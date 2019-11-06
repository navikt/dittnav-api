package no.nav.personbruker.dittnav.api.legacy

import io.ktor.application.call
import io.ktor.client.response.readBytes
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import no.nav.personbruker.dittnav.api.common.extractTokenFromRequest

fun Route.legacyMeldinger(legacyConsumer: LegacyConsumer) {
    get("/person/dittnav-api/meldinger/ubehandlede") {
        val token = extractTokenFromRequest()
        val ubehandledeMeldinger = legacyConsumer.getLegacyContent(
                "meldinger/ubehandlede", token)
        call.respond(ubehandledeMeldinger.status, ubehandledeMeldinger.readBytes())
    }

}

fun Route.legacyPabegynte(legacyConsumer: LegacyConsumer) {
    get("/person/dittnav-api/saker/paabegynte") {
        val token = extractTokenFromRequest()
        val paabegynte = legacyConsumer.getLegacyContent(
                "saker/paabegynte", token)
        call.respond(paabegynte.status, paabegynte.readBytes())
    }
}

fun Route.legacySakstema(legacyConsumer: LegacyConsumer) {
    get("/person/dittnav-api/saker/sakstema") {
        val token = extractTokenFromRequest()
        val sakstema = legacyConsumer.getLegacyContent(
            "saker/sakstema", token)
        call.respond(sakstema.status, sakstema.readBytes())
    }
}

fun Route.legacyPersonnavn(legacyConsumer: LegacyConsumer) {
    get("/person/dittnav-api/personalia/navn") {
        val token = extractTokenFromRequest()
        val navn = legacyConsumer.getLegacyContent(
                "personalia/navn", token)
        call.respond(navn.status, navn.readBytes())
    }
}

fun Route.legacyPersonident(legacyConsumer: LegacyConsumer) {
    get("/person/dittnav-api/personalia/ident") {
        val token = extractTokenFromRequest()
        val ident = legacyConsumer.getLegacyContent(
            "personalia/ident", token)
        call.respond(ident.status, ident.readBytes())
    }
}

fun Route.legacyMeldekortinfo(legacyConsumer: LegacyConsumer) {
    get("/person/dittnav-api/meldekortinfo") {
        val token = extractTokenFromRequest()
        val meldekortinfo = legacyConsumer.getLegacyContent(
            "meldekortinfo", token)
        call.respond(meldekortinfo.status, meldekortinfo.readBytes())
    }
}

fun Route.legacyUnderOppfolging(legacyConsumer: LegacyConsumer) {
    get("/person/dittnav-api/underOppfolging") {
        val token = extractTokenFromRequest()
        val underOppfolging = legacyConsumer.getLegacyContent(
            "underOppfolging", token)
        call.respond(underOppfolging.status, underOppfolging.readBytes())
    }
}

