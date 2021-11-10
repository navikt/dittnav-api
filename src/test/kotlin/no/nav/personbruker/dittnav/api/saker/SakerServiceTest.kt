package no.nav.personbruker.dittnav.api.saker

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import no.finn.unleash.FakeUnleash
import no.nav.personbruker.dittnav.api.common.AuthenticatedUserObjectMother
import no.nav.personbruker.dittnav.api.common.ConsumeSakerException
import no.nav.personbruker.dittnav.api.legacy.LegacyConsumer
import no.nav.personbruker.dittnav.api.tokenx.AccessToken
import no.nav.personbruker.dittnav.api.unleash.UnleashService
import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.`should be instance of`
import org.amshove.kluent.`should contain`
import org.amshove.kluent.shouldNotBeNull
import org.junit.jupiter.api.Test

internal class SakerServiceTest {

    private val legacyConsumer = mockk<LegacyConsumer>(relaxed = true)
    private val mineSakerConsumer = mockk<MineSakerConsumer>(relaxed = true)
    private val tokendings = mockk<MineSakerTokendings>()
    private val dummyUser = AuthenticatedUserObjectMother.createAuthenticatedUser()
    private val urlResolver = SakerInnsynUrlResolver(false)
    private val dummyAccessToken = AccessToken("123")

    init {
        coEvery { tokendings.exchangeToken(any()) } returns dummyAccessToken
    }

    @Test
    fun `Skal hente sakstemer fra Saksoversikt hvis unleash-flagget ikke er satt`() {
        val unleashWithoutAnyFlags = FakeUnleash()
        val unleashService = UnleashService(unleashWithoutAnyFlags)
        coEvery { legacyConsumer.hentSisteEndret(any()) } returns SisteSakstemaerDtoObjectMother.giveMeTemaDagpenger()

        val service = SakerService(mineSakerConsumer, legacyConsumer, unleashService, urlResolver, tokendings)

        val result = runBlocking {
            service.hentSisteToEndredeSakstemaer(dummyUser)
        }

        coVerify(exactly = 1) { legacyConsumer.hentSisteEndret(any()) }
        coVerify(exactly = 0) { tokendings.exchangeToken(any()) }
        coVerify(exactly = 0) { mineSakerConsumer.hentSistEndret(any()) }

        confirmVerified(legacyConsumer)
        confirmVerified(tokendings)
        confirmVerified(mineSakerConsumer)

        result.shouldNotBeNull()
        result.sakerURL.toString() `should contain` "saksoversikt"
    }

    @Test
    fun `Skal hente sakstemaer fra Mine Saker det er aktivert i Unleash`() {
        val unleashWithMineSaker = FakeUnleash().apply {
            enable(UnleashService.brukMineSakerToggleName)
        }
        val unleashService = UnleashService(unleashWithMineSaker)
        coEvery { mineSakerConsumer.hentSistEndret(any()) } returns SisteSakstemaerDtoObjectMother.giveMeTemaDagpenger()

        val service = SakerService(mineSakerConsumer, legacyConsumer, unleashService, urlResolver, tokendings)

        val result = runBlocking {
            service.hentSisteToEndredeSakstemaer(dummyUser)
        }

        coVerify(exactly = 0) { legacyConsumer.hentSisteEndret(any()) }
        coVerify(exactly = 1) { tokendings.exchangeToken(any()) }
        coVerify(exactly = 1) { mineSakerConsumer.hentSistEndret(any()) }

        confirmVerified(legacyConsumer)
        confirmVerified(tokendings)
        confirmVerified(mineSakerConsumer)

        result.shouldNotBeNull()
        result.sakerURL.toString() `should contain` "mine-saker"
    }

    @Test
    fun `Skal hente sakstemaer fra Saksoversikt hvis Mine Saker er deaktivert i Unleash`() {
        val unleashWithMineSaker = FakeUnleash().apply {
            disable(UnleashService.brukMineSakerToggleName)
        }
        val unleashService = UnleashService(unleashWithMineSaker)
        coEvery { legacyConsumer.hentSisteEndret(any()) } returns SisteSakstemaerDtoObjectMother.giveMeTemaDagpenger()

        val service = SakerService(mineSakerConsumer, legacyConsumer, unleashService, urlResolver, tokendings)

        val result = runBlocking {
            service.hentSisteToEndredeSakstemaer(dummyUser)
        }

        coVerify(exactly = 1) { legacyConsumer.hentSisteEndret(any()) }
        coVerify(exactly = 0) { tokendings.exchangeToken(any()) }
        coVerify(exactly = 0) { mineSakerConsumer.hentSistEndret(any()) }

        confirmVerified(legacyConsumer)
        confirmVerified(tokendings)
        confirmVerified(mineSakerConsumer)

        result.shouldNotBeNull()
        result.sakerURL.toString() `should contain` "saksoversikt"
    }

    @Test
    fun `Skal fange og kaste feil videre hvis noe feiler`() {
        val unleashWithMineSaker = FakeUnleash().apply {
            enable(UnleashService.brukMineSakerToggleName)
        }
        val unleashService = UnleashService(unleashWithMineSaker)
        coEvery { mineSakerConsumer.hentSistEndret(any()) } throws IllegalArgumentException("Simulert feil i en test")

        val service = SakerService(mineSakerConsumer, legacyConsumer, unleashService, urlResolver, tokendings)

        val result = runCatching {
            runBlocking {
                service.hentSisteToEndredeSakstemaer(dummyUser)
            }

        }

        result.isFailure `should be equal to` true
        val exception = result.exceptionOrNull()
        exception `should be instance of` ConsumeSakerException::class
        exception?.cause `should be instance of` IllegalArgumentException::class
    }

}
