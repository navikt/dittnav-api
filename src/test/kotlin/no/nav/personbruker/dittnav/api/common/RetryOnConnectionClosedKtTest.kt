package no.nav.personbruker.dittnav.api.common

import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.coInvoking
import org.amshove.kluent.shouldThrow
import org.apache.http.ConnectionClosedException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class RetryOnConnectionLostKtTest {

    @Test
    fun `Should return value of invoked method if less than max retries were needed`() {
        val resourceCall: () -> Int = mockk()

        val resourceValue = 100

        every {
            resourceCall.invoke()
        } throws ConnectionClosedException("") andThen resourceValue

        val result = runBlocking {
            retryOnConnectionClosed(retries = 2,outgoingCall = resourceCall)
        }

        result `should be equal to` resourceValue
    }

    @Test
    fun `Should throw exception if max number of retries was exceeded`() {
        val resourceCall: () -> Int = mockk()

        val resourceValue = 100

        every {
            resourceCall.invoke()
        } throws ConnectionClosedException("") andThenThrows ConnectionClosedException("") andThen resourceValue

        runBlocking {
            coInvoking {
                retryOnConnectionClosed(retries = 2,outgoingCall = resourceCall)
            } shouldThrow ConnectionFailedException::class
        }
    }
}
