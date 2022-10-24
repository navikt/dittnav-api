package no.nav.personbruker.dittnav.api.authentication

import no.nav.personbruker.dittnav.api.TestData
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldNotContain
import org.junit.jupiter.api.Test

internal class AuthenticatedUserTest {

    @Test
    fun `should return expected values`() {
        val expectedIdent = "12345"
        val expectedLoginLevel = 4

        val authenticatedUser = TestData.createAuthenticatedUser(expectedIdent, expectedLoginLevel)

        authenticatedUser.ident shouldBe expectedIdent
        authenticatedUser.loginLevel shouldBe  expectedLoginLevel
        authenticatedUser.token shouldNotBe null
        authenticatedUser.createAuthenticationHeader() shouldBe  "Bearer ${authenticatedUser.token}"
        authenticatedUser shouldNotBe ""
    }

    @Test
    fun `should not include sensitive values in the output for the toString method`() {
        val authenticatedUser = TestData.createAuthenticatedUser()

        val outputOfToString = authenticatedUser.toString()
        outputOfToString shouldContain  authenticatedUser.loginLevel.toString()
        outputOfToString shouldNotContain  authenticatedUser.ident
        outputOfToString shouldNotContain  authenticatedUser.token
    }
}