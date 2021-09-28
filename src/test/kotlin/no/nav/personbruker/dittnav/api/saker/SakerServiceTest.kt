package no.nav.personbruker.dittnav.api.saker

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import no.finn.unleash.FakeUnleash
import no.nav.personbruker.dittnav.api.common.AuthenticatedUserObjectMother
import no.nav.personbruker.dittnav.api.legacy.LegacyConsumer
import no.nav.personbruker.dittnav.api.unleash.UnleashService
import org.amshove.kluent.`should contain`
import org.amshove.kluent.shouldNotBeNull
import org.junit.jupiter.api.Test

internal class SakerServiceTest {

    private val legacyConsumer = mockk<LegacyConsumer>(relaxed = true)
    private val mineSakerConsumer = mockk<MineSakerConsumer>(relaxed = true)
    private val dummyUser = AuthenticatedUserObjectMother.createAuthenticatedUser()
    private val urlResolver = SakerInnsynUrlResolver(false)

    @Test
    fun `Skal hente sakstemer fra Saksoversikt hvis unleash-flagget ikke er satt`() {
        val unleashWithoutAnyFlags = FakeUnleash()
        val unleashService = UnleashService (unleashWithoutAnyFlags)
        coEvery { legacyConsumer.hentSiste(any()) } returns listOf(SakstemaDTOObjectMother.giveMeTemaDagpenger())

        val service = SakerService(mineSakerConsumer, legacyConsumer, unleashService, urlResolver)

        val result = runBlocking {
            service.getSaker(dummyUser)
        }

        coVerify(exactly = 1) { legacyConsumer.hentSiste(any()) }
        coVerify(exactly = 0) { mineSakerConsumer.hentSistEndret(any()) }

        confirmVerified(legacyConsumer)
        confirmVerified(mineSakerConsumer)

        result.shouldNotBeNull()
        result.sakerURL.toString() `should contain` "saksoversikt"
    }

    @Test
    fun `Skal hente sakstemaer fra Mine Saker det er aktivert i Unleash`() {
        val unleashWithMineSaker = FakeUnleash().apply {
            enable(UnleashService.sakerToggleName)
        }
        val unleashService = UnleashService (unleashWithMineSaker)
        coEvery { mineSakerConsumer.hentSistEndret(any()) } returns listOf(SakstemaDTOObjectMother.giveMeTemaDagpenger())

        val service = SakerService(mineSakerConsumer, legacyConsumer, unleashService, urlResolver)

        val result = runBlocking {
            service.getSaker(dummyUser)
        }

        coVerify(exactly = 0) { legacyConsumer.hentSiste(any()) }
        coVerify(exactly = 1) { mineSakerConsumer.hentSistEndret(any()) }

        confirmVerified(legacyConsumer)
        confirmVerified(mineSakerConsumer)

        result.shouldNotBeNull()
        result.sakerURL.toString() `should contain` "mine-saker"
    }

    @Test
    fun `Skal hente sakstemaer fra Saksoversikt hvis Mine Saker ikke er aktivert i Unleash`() {
        val unleashWithoutMineSaker = FakeUnleash()
        val unleashService = UnleashService (unleashWithoutMineSaker)
        coEvery { legacyConsumer.hentSiste(any()) } returns listOf(SakstemaDTOObjectMother.giveMeTemaDagpenger())

        val service = SakerService(mineSakerConsumer, legacyConsumer, unleashService, urlResolver)

        val result = runBlocking {
            service.getSaker(dummyUser)
        }

        coVerify(exactly = 1) { legacyConsumer.hentSiste(any()) }
        coVerify(exactly = 0) { mineSakerConsumer.hentSistEndret(any()) }

        confirmVerified(legacyConsumer)
        confirmVerified(mineSakerConsumer)

        result.shouldNotBeNull()
        result.sakerURL.toString() `should contain` "saksoversikt"
    }

}
