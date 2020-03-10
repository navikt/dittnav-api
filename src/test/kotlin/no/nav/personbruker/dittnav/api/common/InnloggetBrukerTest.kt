package no.nav.personbruker.dittnav.api.common

import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.`should contain`
import org.amshove.kluent.`should not contain`
import org.amshove.kluent.shouldNotBeNullOrBlank
import org.junit.jupiter.api.Test

internal class InnloggetBrukerTest {

    @Test
    fun `should return expected values`() {
        val expectedIdent = "12345"
        val expectedInnloggingsnivaa = 4

        val innloggetbruker = InnloggetBrukerObjectMother.createInnloggetBruker(expectedIdent, expectedInnloggingsnivaa)

        innloggetbruker.ident `should be equal to` expectedIdent
        innloggetbruker.innloggingsnivaa `should be equal to` expectedInnloggingsnivaa
        innloggetbruker.token.shouldNotBeNullOrBlank()
    }

    @Test
    fun `should create authentication header`() {
        val innloggetBruker = InnloggetBrukerObjectMother.createInnloggetBruker()

        val generatedAuthHeader = innloggetBruker.createAuthenticationHeader()

        generatedAuthHeader `should be equal to` "Bearer ${innloggetBruker.token}"
    }

    @Test
    fun `should not include sensitive values in the output for the toString method`() {
        val innloggetBruker = InnloggetBrukerObjectMother.createInnloggetBruker()

        val outputOfToString = innloggetBruker.toString()

        outputOfToString `should contain` (innloggetBruker.innloggingsnivaa.toString())
        outputOfToString `should not contain` (innloggetBruker.ident)
        outputOfToString `should not contain` (innloggetBruker.token)
    }

}
