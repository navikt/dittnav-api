package no.nav.personbruker.dittnav.api.innboks

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import no.nav.personbruker.dittnav.api.common.AuthenticatedUserObjectMother
import no.nav.personbruker.dittnav.api.common.ConsumeEventException
import no.nav.personbruker.dittnav.api.tokenx.AccessToken
import no.nav.personbruker.dittnav.api.tokenx.EventhandlerTokendings
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class InnboksServiceTest {

    private var user = AuthenticatedUserObjectMother.createAuthenticatedUser()
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
        val innboks1 = createInnboks("1", "1", true)
        val innboks2 = createInnboks("2", "2", true)
        coEvery { innboksConsumer.getExternalActiveEvents(dummyToken) } returns listOf(innboks1, innboks2)
        runBlocking {
            val innboksList = innboksService.getActiveInnboksEvents(user)
            innboksList.size shouldBe 2
        }
    }

    @Test
    fun `should return list of InnboksDTO when inactive Events are received`() {
        val innboks1 = createInnboks("1", "1", false)
        val innboks2 = createInnboks("2", "2", false)
        coEvery { innboksConsumer.getExternalInactiveEvents(dummyToken) } returns listOf(innboks1, innboks2)
        runBlocking {
            val innboksList = innboksService.getInactiveInnboksEvents(user)
            innboksList.size shouldBe 2
        }
    }

    @Test
    fun `should mask events with security level higher than current user`() {
        val ident = "1"
        var innboks = createInnboks("1", ident, true)
        innboks = innboks.copy(sikkerhetsnivaa = 4)
        user = AuthenticatedUserObjectMother.createAuthenticatedUser(ident, 3)
        coEvery { eventhandlerTokendings.exchangeToken(user) } returns dummyToken
        coEvery { innboksConsumer.getExternalActiveEvents(dummyToken) } returns listOf(innboks)
        runBlocking {
            val innboksList = innboksService.getActiveInnboksEvents(user)
            val innboksDTO = innboksList.first()
            innboksDTO.tekst shouldBe "***"
            innboksDTO.link shouldBe "***"
            innboksDTO.sikkerhetsnivaa shouldBe 4
        }
    }

    @Test
    fun `should not mask events with security level lower than current user`() {
        var innboks = createInnboks("1", "1", true)
        innboks = innboks.copy(sikkerhetsnivaa = 3)
        coEvery { innboksConsumer.getExternalActiveEvents(dummyToken) } returns listOf(innboks)
        runBlocking {
            val innboksList = innboksService.getActiveInnboksEvents(user)
            val innboksDTO = innboksList.first()
            innboksDTO.tekst shouldBe innboks.tekst
            innboksDTO.link shouldBe innboks.link
            innboksDTO.sikkerhetsnivaa shouldBe 3
        }
    }

    @Test
    fun `should not mask events with security level equal than current user`() {
        val innboks = createInnboks("1", "1", true)
        coEvery { innboksConsumer.getExternalActiveEvents(dummyToken) } returns listOf(innboks)
        runBlocking {
            val innboksList = innboksService.getActiveInnboksEvents(user)
            val innboksDTO = innboksList.first()
            innboksDTO.tekst shouldBe innboks.tekst
            innboksDTO.link shouldBe innboks.link
            innboksDTO.sikkerhetsnivaa shouldBe 4
        }
    }

    @Test
    fun `should throw exception if fetching active events fails`() {
        coEvery { innboksConsumer.getExternalActiveEvents(dummyToken) } throws Exception("error")
        shouldThrow<ConsumeEventException> { runBlocking { innboksService.getActiveInnboksEvents(user) } }
    }

    @Test
    fun `should throw exception if fetching inactive events fails`() {
        coEvery { innboksConsumer.getExternalInactiveEvents(dummyToken) } throws Exception("error")
        shouldThrow<ConsumeEventException> { runBlocking { innboksService.getInactiveInnboksEvents(user) } }
    }
}
