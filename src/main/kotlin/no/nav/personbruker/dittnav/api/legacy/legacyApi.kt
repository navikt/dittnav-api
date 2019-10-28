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

fun Route.legacyPersoninfo(legacyConsumer: LegacyConsumer) {
    get("/person/dittnav-api/person/personinfo") {
        val token = extractTokenFromRequest()
        val personinfo = legacyConsumer.getLegacyContent(
                "person/personinfo", token)
        call.respond(personinfo.status, personinfo.readBytes())
    }
}

