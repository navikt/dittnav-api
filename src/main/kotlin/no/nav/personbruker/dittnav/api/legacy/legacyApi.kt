package no.nav.personbruker.dittnav.api.legacy

import io.ktor.application.call
import io.ktor.client.response.readBytes
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import no.nav.personbruker.dittnav.api.common.extractTokenFromRequest

fun Route.legacyApi(legacyConsumer: LegacyConsumer) {

    val ubehandledeMeldingerPath = "/meldinger/ubehandlede"
    val paabegynteSakerPath = "/saker/paabegynte"
    val sakstemaPath = "/saker/sakstema"
    val navnPath = "/personalia/navn"
    val identPath = "/personalia/ident"
    val meldekortPath = "/meldekortinfo"
    val oppfolgingPath = "/oppfolging"

    get(ubehandledeMeldingerPath) {
        val token = extractTokenFromRequest()
        val ubehandledeMeldinger = legacyConsumer.getLegacyContent(
                ubehandledeMeldingerPath,
                token
        )
        call.respond(ubehandledeMeldinger.status, ubehandledeMeldinger.readBytes())
    }

    get(paabegynteSakerPath) {
        val token = extractTokenFromRequest()
        val paabegynte = legacyConsumer.getLegacyContent(
                paabegynteSakerPath,
                token
        )
        call.respond(paabegynte.status, paabegynte.readBytes())
    }

    get(sakstemaPath) {
        val token = extractTokenFromRequest()
        val sakstema = legacyConsumer.getLegacyContent(
                sakstemaPath, token)
        call.respond(sakstema.status, sakstema.readBytes())
    }

    get(navnPath) {
        val token = extractTokenFromRequest()
        val navn = legacyConsumer.getLegacyContent(
                navnPath,
                token
        )
        call.respond(navn.status, navn.readBytes())
    }

    get(identPath) {
        val token = extractTokenFromRequest()
        val ident = legacyConsumer.getLegacyContent(
                identPath, token)
        call.respond(ident.status, ident.readBytes())
    }

    get(meldekortPath) {
        val token = extractTokenFromRequest()
        val meldekortinfo = legacyConsumer.getLegacyContent(
                meldekortPath,
                token
        )
        call.respond(meldekortinfo.status, meldekortinfo.readBytes())
    }

    get(oppfolgingPath) {
        val token = extractTokenFromRequest()
        val oppfolging = legacyConsumer.getLegacyContent(
                oppfolgingPath,
                token
        )
        call.respond(oppfolging.status, oppfolging.readBytes())
    }

}
