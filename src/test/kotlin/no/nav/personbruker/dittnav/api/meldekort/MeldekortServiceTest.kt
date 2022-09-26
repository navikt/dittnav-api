package no.nav.personbruker.dittnav.api.meldekort

import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import no.nav.personbruker.dittnav.api.common.AuthenticatedUserObjectMother
import no.nav.personbruker.dittnav.api.tokenx.AccessToken
import org.junit.jupiter.api.Test

internal class MeldekortServiceTest {

    private val consumer: MeldekortConsumer = mockk()
    private val tokendings: MeldekortTokendings = mockk()
    private val meldekortService = MeldekortService(consumer, tokendings)

    private val user = AuthenticatedUserObjectMother.createAuthenticatedUser("123")
    private val token = AccessToken(user.token)

    @Test
    fun `should fetch and transform external meldekortstatus`() {
        val externalMeldekortStatus = createMeldekortStatus()

        coEvery {
            tokendings.exchangeToken(user)
        } returns token

        coEvery {
            consumer.getMeldekortStatus(token)
        } returns externalMeldekortStatus

        val result = runBlocking {
            meldekortService.getMeldekortInfo(user)
        }

        result.meldekortbruker shouldBe true
        result.nyeMeldekort.nesteMeldekort.shouldNotBeNull()

        coVerify(exactly = 1) { consumer.getMeldekortStatus(AccessToken(user.token)) }
    }
}
