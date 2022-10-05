package no.nav.personbruker.dittnav.api.saker

import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import no.nav.personbruker.dittnav.api.authentication.AuthenticatedUserTestData
import no.nav.personbruker.dittnav.api.common.ConsumeSakerException
import no.nav.personbruker.dittnav.api.tokenx.AccessToken
import org.junit.jupiter.api.Test
import java.net.URL
import java.time.ZonedDateTime

internal class SakerServiceTest {

    private val mineSakerConsumer = mockk<MineSakerConsumer>(relaxed = true)
    private val mineSakerURL = URL("http://mine-saker")
    private val tokendings = mockk<MineSakerTokendings>()
    private val dummyUser = AuthenticatedUserTestData.createAuthenticatedUser()
    private val dummyAccessToken = AccessToken("123")

    init {
        coEvery { tokendings.exchangeToken(any()) } returns dummyAccessToken
    }

    @Test
    fun `Skal hente sakstemaer fra Mine Saker`() {

        coEvery { mineSakerConsumer.hentSistEndret(any()) } returns SisteSakstemaerDTO(
            listOf(SakstemaTestData.temaDagpenger(), SakstemaTestData.temaBil()),
            ZonedDateTime.now()
        )


        val service = SakerService(mineSakerConsumer, mineSakerURL, tokendings)

        val result = runBlocking {
            service.hentSisteToEndredeSakstemaer(dummyUser)
        }

        coVerify(exactly = 1) { tokendings.exchangeToken(any()) }
        coVerify(exactly = 1) { mineSakerConsumer.hentSistEndret(any()) }

        confirmVerified(tokendings)
        confirmVerified(mineSakerConsumer)

        result.shouldNotBeNull()
        result.sakerURL.toString() shouldContain "mine-saker"
    }

    @Test
    fun `Skal fange og kaste feil videre hvis noe feiler`() {

        coEvery { mineSakerConsumer.hentSistEndret(any()) } throws IllegalArgumentException("Simulert feil i en test")

        val service = SakerService(mineSakerConsumer, mineSakerURL, tokendings)

        val result = runCatching {
            runBlocking {
                service.hentSisteToEndredeSakstemaer(dummyUser)
            }

        }

        result.isFailure shouldBe true
        val exception = result.exceptionOrNull()
        exception!!::class shouldBe ConsumeSakerException::class
        exception.cause!!::class shouldBe IllegalArgumentException::class
    }

}