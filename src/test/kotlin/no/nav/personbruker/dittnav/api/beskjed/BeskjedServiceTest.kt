package no.nav.personbruker.dittnav.api.beskjed

import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import no.nav.personbruker.dittnav.api.authentication.AuthenticatedUserTestData
import no.nav.personbruker.dittnav.api.tokenx.AccessToken
import no.nav.personbruker.dittnav.api.tokenx.EventhandlerTokendings
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class BeskjedServiceTest {

    private var user = AuthenticatedUserTestData.createAuthenticatedUser()
    private val dummyToken = AccessToken("<access_token>")

    private val beskjedConsumer = mockk<BeskjedConsumer>()
    private val eventhandlerTokendings = mockk<EventhandlerTokendings>()
    private val beskjedService = BeskjedService(beskjedConsumer, eventhandlerTokendings)

    @BeforeEach
    fun setup() {
        coEvery { eventhandlerTokendings.exchangeToken(user) } returns dummyToken
    }

    @Test
    fun `should return list of BeskjedDTO when active Events are received`() {
        val beskjed1 = createBeskjed(eventId = "1", fodselsnummer = "1", aktiv = true)
        val beskjed2 = createBeskjed(eventId = "2", fodselsnummer = "2", aktiv = true)
        coEvery { beskjedConsumer.getExternalActiveEvents(dummyToken) } returns listOf(beskjed1, beskjed2)
        runBlocking {
            val beskjedResult = beskjedService.getActiveBeskjedEvents(user)
            beskjedResult.results().size shouldBe 2
        }
    }

    @Test
    fun `should return list of BeskjedDTO when inactive Events are received`() {
        val beskjed1 = createBeskjed(eventId = "1", fodselsnummer = "1", aktiv = false)
        val beskjed2 = createBeskjed(eventId = "2", fodselsnummer = "2", aktiv = false)
        coEvery { beskjedConsumer.getExternalInactiveEvents(dummyToken) } returns listOf(beskjed1, beskjed2)
        runBlocking {
            val beskjedResult = beskjedService.getInactiveBeskjedEvents(user)
            beskjedResult.results().size shouldBe 2
        }
    }

    @Test
    fun `should mask events with security level higher than current user`() {
        val ident = "1"
        var beskjed = createBeskjed(eventId = "1", fodselsnummer = ident, aktiv = true)
        beskjed = beskjed.copy(sikkerhetsnivaa = 4)
        user = AuthenticatedUserTestData.createAuthenticatedUser(ident, 3)
        coEvery { eventhandlerTokendings.exchangeToken(user) } returns dummyToken
        coEvery { beskjedConsumer.getExternalActiveEvents(dummyToken) } returns listOf(beskjed)
        runBlocking {
            val beskjedResult = beskjedService.getActiveBeskjedEvents(user)
            val beskjedDTO = beskjedResult.results().first()
            beskjedDTO.tekst shouldBe "***"
            beskjedDTO.link shouldBe "***"
            beskjedDTO.sikkerhetsnivaa shouldBe 4
        }
    }

    @Test
    fun `should not mask events with security level lower than current user`() {
        var beskjed = createBeskjed(eventId = "1", fodselsnummer = "1", aktiv = true)
        beskjed = beskjed.copy(sikkerhetsnivaa = 3)
        coEvery { beskjedConsumer.getExternalActiveEvents(dummyToken) } returns listOf(beskjed)
        runBlocking {
            val beskjedResult = beskjedService.getActiveBeskjedEvents(user)
            val beskjedDTO = beskjedResult.results().first()
            beskjedDTO.tekst shouldBe beskjed.tekst
            beskjedDTO.link shouldBe beskjed.link
            beskjedDTO.sikkerhetsnivaa shouldBe 3
        }
    }

    @Test
    fun `should not mask events with security level equal than current user`() {
        val beskjed = createBeskjed(eventId = "1", fodselsnummer = "1", aktiv = true)
        coEvery { beskjedConsumer.getExternalActiveEvents(dummyToken) } returns listOf(beskjed)
        runBlocking {
            val beskjedResult = beskjedService.getActiveBeskjedEvents(user)
            val beskjedDTO = beskjedResult.results().first()
            beskjedDTO.tekst shouldBe beskjed.tekst
            beskjedDTO.link shouldBe beskjed.link
            beskjedDTO.sikkerhetsnivaa shouldBe 4
        }
    }

    @Test
    fun `should throw exception if fetching active events fails`() {
        coEvery { beskjedConsumer.getExternalActiveEvents(dummyToken) } throws Exception("error")
        runBlocking {
            val beskjedResult = beskjedService.getActiveBeskjedEvents(user)
            beskjedResult.hasErrors() shouldBe true
            beskjedResult.failedSources() shouldContain KildeType.EVENTHANDLER
        }
    }

    @Test
    fun `should throw exception if fetching inactive events fails`() {
        coEvery { beskjedConsumer.getExternalInactiveEvents(dummyToken) } throws Exception("error")
        runBlocking {
            val beskjedResult = beskjedService.getInactiveBeskjedEvents(user)
            beskjedResult.hasErrors() shouldBe true
            beskjedResult.failedSources() shouldContain KildeType.EVENTHANDLER
        }
    }
}
