package no.nav.personbruker.dittnav.api.common

import io.mockk.coEvery
import kotlinx.coroutines.runBlocking
import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.`should throw`
import org.amshove.kluent.invoking
import org.junit.jupiter.api.Test

internal class InnloggetBrukerTest {

    val tokenSupport = createDummyTokenSupport()
    val innloggetBruker = InnloggetBruker(tokenSupport)

    @Test
    fun `should return string with token`() {
        val expectedToken = "dummyToken"

        coEvery({ tokenSupport.context.firstValidToken.get().tokenAsString }) returns expectedToken

        runBlocking {
            val tokenAsString = innloggetBruker.getTokenAsString()
            tokenAsString `should be equal to` expectedToken
        }
    }

    @Test
    fun `should return string with Bearer token`() {
        val expectedToken = "Bearer dummyToken"
        val dummyToken = "dummyToken"

        coEvery({ tokenSupport.context.firstValidToken.get().tokenAsString }) returns dummyToken

        runBlocking {
            val bearerToken = innloggetBruker.getBearerToken()
            bearerToken `should be equal to` expectedToken
        }
    }

    @Test
    fun `should throw Exception if token is null`() {

        coEvery({ tokenSupport.context.firstValidToken.get().tokenAsString }) returns null

        invoking {
            runBlocking {
                innloggetBruker.getBearerToken()
            }

        } `should throw` Exception::class
    }
}