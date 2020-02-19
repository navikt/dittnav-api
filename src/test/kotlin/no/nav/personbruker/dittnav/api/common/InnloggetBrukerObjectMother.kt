package no.nav.personbruker.dittnav.api.common

import io.mockk.every
import io.mockk.mockk
import no.nav.security.token.support.core.jwt.JwtToken

object InnloggetBrukerObjectMother {

    fun createInnloggetBruker(): InnloggetBruker {
        val dummyJwtToken = mockk<JwtToken>()
        val dummyTokenAsString = "dummyToken"
        every { dummyJwtToken.tokenAsString} returns dummyTokenAsString
        return InnloggetBruker(dummyJwtToken)
    }
}