package no.nav.personbruker.dittnav.api.meldekort

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import no.nav.personbruker.dittnav.api.common.AuthenticatedUserObjectMother
import no.nav.personbruker.dittnav.api.tokenx.AccessToken
import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.`should not be equal to`
import org.junit.jupiter.api.Test

internal class MeldekortServiceTest {

    private val consumer: MeldekortConsumer = mockk()
    private val meldekortService = MeldekortService(consumer)

    private val user = AuthenticatedUserObjectMother.createAuthenticatedUser("123")

    @Test
    fun `should fetch and transform external meldekortstatus`() {
        val externalMeldekortStatus = MeldekortExternalObjectMother.createMeldekortStatus()

        coEvery {
            consumer.getMeldekortStatus(AccessToken(user.token))
        } returns externalMeldekortStatus

        val result = runBlocking {
            meldekortService.getMeldekortInfo(user)
        }

        result.meldekortbruker `should be equal to` true
        result.nyeMeldekort.nesteMeldekort `should not be equal to` null

        coVerify(exactly = 1) { consumer.getMeldekortStatus(AccessToken(user.token)) }
    }
}
