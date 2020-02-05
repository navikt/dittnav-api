package no.nav.personbruker.dittnav.api.common

import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.auth.authentication
import io.ktor.util.pipeline.PipelineContext
import no.nav.security.token.support.ktor.OIDCValidationContextPrincipal

fun PipelineContext<Unit, ApplicationCall>.hentInnloggetBruker(): InnloggetBruker {

    val innloggetBruker = InnloggetBruker(call.authentication.principal<OIDCValidationContextPrincipal>())

    if (innloggetBruker.principal == null) {
        throw Exception("Det ble ikke funnet noe token. Dette skal ikke kunne skje.")
    }
    return innloggetBruker
}
