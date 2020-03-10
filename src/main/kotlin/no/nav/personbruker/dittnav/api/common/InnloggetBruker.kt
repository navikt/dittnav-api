package no.nav.personbruker.dittnav.api.common

data class InnloggetBruker(val ident: String, val innloggingsnivaa: Int, val token: String) {

    fun createAuthenticationHeader(): String {
        return "Bearer $token"
    }

    override fun toString(): String {
        return "InnloggetBruker(ident='***', innloggingsnivaa=$innloggingsnivaa, token='***')"
    }

}
