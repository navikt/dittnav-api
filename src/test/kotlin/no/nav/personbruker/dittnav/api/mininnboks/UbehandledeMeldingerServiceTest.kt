package no.nav.personbruker.dittnav.api.mininnboks

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import no.nav.personbruker.dittnav.api.common.AuthenticatedUserObjectMother
import no.nav.personbruker.dittnav.api.mininnboks.external.UbehandletMeldingExternal
import no.nav.personbruker.dittnav.api.tokenx.AccessToken
import org.amshove.kluent.`should be equal to`
import org.junit.jupiter.api.Test

internal class UbehandledeMeldingerServiceTest {

    private val consumer: MininnboksConsumer = mockk()
    private val transformer: UbehandledeMeldingerTransformer = mockk()
    private val ubehandledeMeldingerService = UbehandledeMeldingerService(consumer, transformer)

    private val user = AuthenticatedUserObjectMother.createAuthenticatedUser("123")

    @Test
    fun `should fetch and transform external meldekortstatus`() {
        val external: List<UbehandletMeldingExternal> = mockk()

        coEvery {
            consumer.getUbehandledeMeldinger(AccessToken(user.token))
        } returns external

        val internal: List<UbehandledeMeldinger> = mockk()

        every {
            transformer.toInternal(external)
        } returns internal

        val result = runBlocking {
            ubehandledeMeldingerService.getUbehandledeMeldinger(user)
        }

        result `should be equal to` internal

        coVerify(exactly = 1) { consumer.getUbehandledeMeldinger(AccessToken(user.token)) }
        coVerify(exactly = 1) { transformer.toInternal(external) }
    }
}
