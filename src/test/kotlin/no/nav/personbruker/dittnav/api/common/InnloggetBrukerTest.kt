package no.nav.personbruker.dittnav.api.common

import io.mockk.coEvery
import kotlinx.coroutines.runBlocking
import org.amshove.kluent.`should be equal to`
import org.junit.jupiter.api.Test

internal class InnloggetBrukerTest {

    val innloggetBruker = InnloggetBrukerObjectMother.createInnloggetBruker()

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
        val expectedIdent = "dummyIdent"

        coEvery { innloggetBruker.token.jwtTokenClaims.getStringClaim("pid")} returns null
        coEvery { innloggetBruker.token.jwtTokenClaims.getStringClaim("sub")} returns expectedIdent

        runBlocking {
            val acctualIdent = innloggetBruker.getIdentFromToken()
            acctualIdent `should be equal to` expectedIdent
        }
    }
}