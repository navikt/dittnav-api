package no.nav.personbruker.dittnav.api.common

import io.mockk.coEvery
import kotlinx.coroutines.runBlocking
import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.`should equal`
import org.junit.jupiter.api.Test

internal class InnloggetBrukerTest {

    val innloggetBruker = InnloggetBrukerObjectMother.createInnloggetBruker(SecurityLevel.Level4)

    @Test
    fun `should return string with Bearer token`() {
        val expectedToken = "Bearer dummyToken"

        runBlocking {
            val actualToken = innloggetBruker.getBearerToken()
            actualToken `should be equal to` expectedToken
        }
    }

    @Test
    fun `should return string with ident from pid token claim`() {
        val expectedIdent = "dummyIdent"
        val subClaimThatIsNotAnIdent ="dummyClaimThatIsNotAnIdent"

        coEvery { innloggetBruker.token.jwtTokenClaims.getStringClaim("pid")} returns expectedIdent
        coEvery { innloggetBruker.token.jwtTokenClaims.getStringClaim("sub")} returns subClaimThatIsNotAnIdent

        runBlocking {
            val actualIdent = innloggetBruker.getIdentFromToken()
            actualIdent `should be equal to` expectedIdent
        }
    }

    @Test
    fun `should return string with ident from sub token claim`() {
        val expectedIdent = "123"

        coEvery { innloggetBruker.token.jwtTokenClaims.getStringClaim("pid")} returns null
        coEvery { innloggetBruker.token.jwtTokenClaims.getStringClaim("sub")} returns expectedIdent

        runBlocking {
            val actualIdent = innloggetBruker.getIdentFromToken()
            actualIdent `should be equal to` expectedIdent
        }
    }

    @Test
    fun `should return security level 4 from acr token claim`() {
        val expectedLevel = SecurityLevel.Level4
        coEvery { innloggetBruker.token.jwtTokenClaims.getStringClaim("acr")} returns "Level4"

        runBlocking {
            val actualLevel = innloggetBruker.getSecurityLevel()
            actualLevel `should equal` expectedLevel
        }
    }

    @Test
    fun `should return security level 3 from acr token claim`() {
        val expectedLevel = SecurityLevel.Level3
        coEvery { innloggetBruker.token.jwtTokenClaims.getStringClaim("acr")} returns "Level3"

        runBlocking {
            val actualLevel = innloggetBruker.getSecurityLevel()
            actualLevel `should equal` expectedLevel
        }
    }

    @Test
    fun `should return unknown level from acr token claim`() {
        val expectedLevel = SecurityLevel.Ukjent
        coEvery { innloggetBruker.token.jwtTokenClaims.getStringClaim("acr")} returns "dummy"

        runBlocking {
            val actualLevel = innloggetBruker.getSecurityLevel()
            actualLevel `should equal` expectedLevel
        }
    }

    @Test
    fun `should return int indicating unknown level if acr claim is missing`() {
        val expectedLevel = SecurityLevel.Ukjent
        coEvery { innloggetBruker.token.jwtTokenClaims.getStringClaim("acr")} returns null

        runBlocking {
            val actualLevel = innloggetBruker.getSecurityLevel()
            actualLevel `should equal` expectedLevel
        }
    }
}
