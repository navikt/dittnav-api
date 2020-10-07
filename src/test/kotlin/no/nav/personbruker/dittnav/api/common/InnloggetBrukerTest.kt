package no.nav.personbruker.dittnav.api.common

import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.`should contain`
import org.amshove.kluent.`should not contain`
import org.amshove.kluent.shouldNotBeNullOrBlank
import org.junit.jupiter.api.Test
import java.time.ZonedDateTime

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

    @Test
    fun `should return false for users with expired token`() {
        val inThePast = ZonedDateTime.now().minusSeconds(120)
        val bruker = InnloggetBrukerObjectMother.createInnloggetBrukerWithValidTokenUntil("123", 4, inThePast)

        bruker.isTokenExpired() `should be equal to` true
    }

    @Test
    fun `should return true for users with valid token`() {
        val inTheFuture = ZonedDateTime.now().plusSeconds(120)
        val bruker = InnloggetBrukerObjectMother.createInnloggetBrukerWithValidTokenUntil("123", 4, inTheFuture)

        bruker.isTokenExpired() `should be equal to` false
    }

    @Test
    fun `should return true for users with valid token that is about to expire`() {
        val inTheFuture = ZonedDateTime.now().plusMinutes(1)
        val expiryThresholdInMinutes = 2L
        val bruker = InnloggetBrukerObjectMother.createInnloggetBrukerWithValidTokenUntil("123", 4, inTheFuture)

        bruker.isTokenAboutToExpire(expiryThresholdInMinutes) `should be equal to` true
    }

    @Test
    fun `should return false for users with valid token that is about to expire`() {
        val inTheFuture = ZonedDateTime.now().plusMinutes(3)
        val expiryThresholdInMinutes = 2L
        val bruker = InnloggetBrukerObjectMother.createInnloggetBrukerWithValidTokenUntil("123", 4, inTheFuture)

        bruker.isTokenAboutToExpire(expiryThresholdInMinutes) `should be equal to` false
    }

}
