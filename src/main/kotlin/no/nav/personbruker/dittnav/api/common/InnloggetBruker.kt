package no.nav.personbruker.dittnav.api.common

import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.auth.authentication
import io.ktor.util.pipeline.PipelineContext

data class InnloggetBruker(val ident: String, val innloggingsnivaa: Int, val token: String) {

    fun createAuthenticationHeader(): String {
        return "Bearer $token"
    }

    override fun toString(): String {
        return "InnloggetBruker(ident='***', innloggingsnivaa=$innloggingsnivaa, token='***')"
    }

}

val PipelineContext<Unit, ApplicationCall>.innloggetBruker: InnloggetBruker
    get() = InnloggetBrukerFactory.createNewInnloggetBruker(call.authentication.principal())
