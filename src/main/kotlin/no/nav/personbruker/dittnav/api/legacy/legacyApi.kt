package no.nav.personbruker.dittnav.api.legacy

import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.client.statement.readBytes
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.util.pipeline.PipelineContext
import no.nav.personbruker.dittnav.api.common.hentInnloggetBruker

fun Route.legacyApi(legacyConsumer: LegacyConsumer) {

    val ubehandledeMeldingerPath = "/meldinger/ubehandlede"
    val paabegynteSakerPath = "/saker/paabegynte"
    val sakstemaPath = "/saker/sakstema"
    val navnPath = "/personalia/navn"
    val identPath = "/personalia/ident"
    val meldekortPath = "/meldekortinfo"
    val oppfolgingPath = "/oppfolging"

    get(ubehandledeMeldingerPath) {
        hentRaattFraLegacyApiOgReturnerResponsen(legacyConsumer, ubehandledeMeldingerPath)
    }

    get(paabegynteSakerPath) {
        hentRaattFraLegacyApiOgReturnerResponsen(legacyConsumer, paabegynteSakerPath)
    }

    get(sakstemaPath) {
        hentRaattFraLegacyApiOgReturnerResponsen(legacyConsumer, sakstemaPath)
    }

    get(navnPath) {
        hentRaattFraLegacyApiOgReturnerResponsen(legacyConsumer, navnPath)
    }

    get(identPath) {
        hentRaattFraLegacyApiOgReturnerResponsen(legacyConsumer, identPath)
    }

    get(meldekortPath) {
        hentRaattFraLegacyApiOgReturnerResponsen(legacyConsumer, meldekortPath)
    }

    get(oppfolgingPath) {
        hentRaattFraLegacyApiOgReturnerResponsen(legacyConsumer, oppfolgingPath)
    }

}

private suspend fun PipelineContext<Unit, ApplicationCall>.hentRaattFraLegacyApiOgReturnerResponsen(consumer: LegacyConsumer, path: String) {
    val response = consumer.getLegacyContent(path, hentInnloggetBruker())
    call.respond(response.status, response.readBytes())
}
