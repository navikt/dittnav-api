package no.nav.personbruker.dittnav.api.innboks

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import no.nav.personbruker.dittnav.api.TestUser
import no.nav.personbruker.dittnav.api.config.ConsumeEventException
import no.nav.personbruker.dittnav.api.tokenx.AccessToken
import no.nav.personbruker.dittnav.api.tokenx.EventhandlerTokendings
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class InnboksServiceTest {

    private var user = TestUser.createAuthenticatedUser()
    private val dummyToken = AccessToken("<access_token>")

    private val innboksConsumer = mockk<InnboksConsumer>()
    private val eventhandlerTokendings = mockk<EventhandlerTokendings>()
    private val innboksService = InnboksService(innboksConsumer, eventhandlerTokendings)

    @BeforeEach
    fun setup() {
        coEvery { eventhandlerTokendings.exchangeToken(user) } returns dummyToken
    }

    @Test
    fun `should return list of InnboksDTO when active Events are received`() {
        val innboks1 = createInnboks(eventId = "1", fodselsnummer = "1", aktiv = true)
        val innboks2 = createInnboks(eventId = "2", fodselsnummer = "2", aktiv = true)
        coEvery { innboksConsumer.getExternalActiveEvents(dummyToken) } returns listOf(innboks1, innboks2)
        runBlocking {
            val innboksList = innboksService.getActiveInnboksEvents(user)
            innboksList.size shouldBe 2
        }
    }

    @Test
    fun `should return list of InnboksDTO when inactive Events are received`() {
        val innboks1 = createInnboks(eventId = "1", fodselsnummer = "1", aktiv = false)
        val innboks2 = createInnboks(eventId = "2", fodselsnummer = "2", aktiv = false)
        coEvery { innboksConsumer.getExternalInactiveEvents(dummyToken) } returns listOf(innboks1, innboks2)
        runBlocking {
            val innboksList = innboksService.getInactiveInnboksEvents(user)
            innboksList.size shouldBe 2
        }
    }

    @Test
    fun `should throw exception if fetching events fails`() {
        coEvery { innboksConsumer.getExternalActiveEvents(dummyToken) } throws Exception("error")
        coEvery { innboksConsumer.getExternalInactiveEvents(dummyToken) } throws Exception("error")

        shouldThrow<ConsumeEventException> { runBlocking { innboksService.getActiveInnboksEvents(user) } }
        shouldThrow<ConsumeEventException> { runBlocking { innboksService.getInactiveInnboksEvents(user) } }
    }
}
