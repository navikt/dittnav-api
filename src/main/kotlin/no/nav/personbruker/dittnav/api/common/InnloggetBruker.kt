package no.nav.personbruker.dittnav.api.common

import java.util.*

data class InnloggetBruker(val ident: String,
                           val innloggingsnivaa: Int,
                           val token: String,
                           val tokenExpirationTime: Date) {

    fun createAuthenticationHeader(): String {
        return "Bearer $token"
    }

    override fun toString(): String {
        return "InnloggetBruker(ident='***', innloggingsnivaa=$innloggingsnivaa, token='***', tokenUtlopsdato: $tokenExpirationTime)"
    }

}
