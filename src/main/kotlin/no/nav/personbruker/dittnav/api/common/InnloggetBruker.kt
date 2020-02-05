package no.nav.personbruker.dittnav.api.common

import no.nav.security.token.support.ktor.OIDCValidationContextPrincipal

class InnloggetBruker(val principal: OIDCValidationContextPrincipal?) {

    fun getBearerToken(): String {
        return "Bearer " + getTokenAsString()
    }

    fun getTokenAsString(): String {
        val token = principal?.context?.firstValidToken?.get()?.tokenAsString
        if (token == null) {
            throw Exception("Det ble ikke funnet noe token. Dette skal ikke kunne skje..")
        } else {
            return token
        }
    }
}