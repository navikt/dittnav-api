package no.nav.personbruker.dittnav.api.common

import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.auth.authentication
import io.ktor.util.pipeline.PipelineContext
import no.nav.security.token.support.ktor.OIDCValidationContextPrincipal

fun PipelineContext<Unit, ApplicationCall>.hentInnloggetBruker(): InnloggetBruker {

    val token = call.authentication.principal<OIDCValidationContextPrincipal>()?.context?.firstValidToken

    if (token == null) {
        throw Exception("Det ble ikke funnet noe token. Dette skal ikke kunne skje.")
    } else {
        return InnloggetBruker(token.get())
    }
}

val PipelineContext<Unit, ApplicationCall>.innloggetBruker: InnloggetBruker get() = hentInnloggetBruker()