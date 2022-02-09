package no.nav.personbruker.dittnav.api.personalia

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import no.nav.personbruker.dittnav.api.common.AuthenticatedUserObjectMother
import no.nav.personbruker.dittnav.api.common.ConsumePersonaliaException
import no.nav.personbruker.dittnav.api.tokenx.AccessToken
import org.amshove.kluent.*
import org.junit.jupiter.api.Test

internal class PersonaliaServiceTest {
    private val personaliaConsumer = mockk<PersonaliaConsumer>(relaxed = true)
    private val tokendings = mockk<PersonaliaTokendings>()
    private val dummyUser = AuthenticatedUserObjectMother.createAuthenticatedUser()
    private val dummyAccessToken = AccessToken("123")

    init {
        coEvery { tokendings.exchangeToken(any()) } returns dummyAccessToken
    }

    @Test
    fun `Skal hente navn fra personalia api`() {
        coEvery { personaliaConsumer.hentNavn(any()) } returns PersonaliaObjectMother.giveMeNavn()

        val service = PersonaliaService(personaliaConsumer, tokendings)

        val result = runBlocking {
            service.hentNavn(dummyUser)
        }

        coVerify(exactly = 1) { tokendings.exchangeToken(any()) }
        coVerify(exactly = 1) { personaliaConsumer.hentNavn(any()) }

        confirmVerified(tokendings)
        confirmVerified(personaliaConsumer)

        result.shouldNotBeNull()
        result.navn `should be` "TestName"
    }

    @Test
    fun `Skal fange og kaste feil videre hvis henting av navn feiler`() {
        coEvery { personaliaConsumer.hentNavn(any()) } throws IllegalArgumentException("Simulert feil i en test")

        val service = PersonaliaService(personaliaConsumer, tokendings)

        val result = runCatching {
            runBlocking {
                service.hentNavn(dummyUser)
            }

        }

        result.isFailure `should be equal to` true
        val exception = result.exceptionOrNull()
        exception `should be instance of` ConsumePersonaliaException::class
        exception?.cause `should be instance of` IllegalArgumentException::class
    }

    @Test
    fun `Skal hente ident fra personalia api`() {
        coEvery { personaliaConsumer.hentIdent(any()) } returns PersonaliaObjectMother.giveMeIdent()

        val service = PersonaliaService(personaliaConsumer, tokendings)

        val result = runBlocking {
            service.hentIdent(dummyUser)
        }

        coVerify(exactly = 1) { tokendings.exchangeToken(any()) }
        coVerify(exactly = 1) { personaliaConsumer.hentIdent(any()) }

        confirmVerified(tokendings)
        confirmVerified(personaliaConsumer)

        result.shouldNotBeNull()
        result.ident `should be` "1234"
    }

    @Test
    fun `Skal fange og kaste feil videre hvis henting av ident feiler`() {
        coEvery { personaliaConsumer.hentIdent(any()) } throws IllegalArgumentException("Simulert feil i en test")

        val service = PersonaliaService(personaliaConsumer, tokendings)

        val result = runCatching {
            runBlocking {
                service.hentIdent(dummyUser)
            }

        }

        result.isFailure `should be equal to` true
        val exception = result.exceptionOrNull()
        exception `should be instance of` ConsumePersonaliaException::class
        exception?.cause `should be instance of` IllegalArgumentException::class
    }

}
