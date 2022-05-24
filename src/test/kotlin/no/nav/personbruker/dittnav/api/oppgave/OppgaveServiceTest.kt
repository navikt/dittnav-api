package no.nav.personbruker.dittnav.api.oppgave

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

internal class OppgaveServiceTest {

    private var user = AuthenticatedUserObjectMother.createAuthenticatedUser()
    private val dummyToken = AccessToken("<access_token>")

    private val oppgaveConsumer = mockk<OppgaveConsumer>()
    private val eventhandlerTokendings = mockk<EventhandlerTokendings>()
    private val oppgaveService = OppgaveService(oppgaveConsumer, eventhandlerTokendings)

    @BeforeEach
    fun setup() {
        coEvery { eventhandlerTokendings.exchangeToken(user) } returns dummyToken
    }

    @Test
    fun `should return list of OppgaveDTO when active Events are received`() {
        val oppgave1 = createOppgave("1", "1", true)
        val oppgave2 = createOppgave("2", "2", true)
        coEvery { oppgaveConsumer.getExternalActiveEvents(dummyToken) } returns listOf(oppgave1, oppgave2)
        runBlocking {
            val oppgaveList = oppgaveService.getActiveOppgaver(user)
            oppgaveList.size shouldBe 2
        }
    }

    @Test
    fun `should return list of OppgaveDTO when inactive Events are received`() {
        val oppgave1 = createOppgave("1", "1", false)
        val oppgave2 = createOppgave("2", "2", false)
        coEvery { oppgaveConsumer.getExternalInactiveEvents(dummyToken) } returns listOf(oppgave1, oppgave2)
        runBlocking {
            val oppgaveList = oppgaveService.getInactiveOppgaver(user)
            oppgaveList.size shouldBe 2
        }
    }

    @Test
    fun `should mask events with security level higher than current user`() {
        val ident = "1"
        var oppgave = createOppgave("1", ident, true)
        oppgave = oppgave.copy(sikkerhetsnivaa = 4)
        user = AuthenticatedUserObjectMother.createAuthenticatedUser(ident, 3)
        coEvery { eventhandlerTokendings.exchangeToken(user) } returns dummyToken
        coEvery { oppgaveConsumer.getExternalActiveEvents(dummyToken) } returns listOf(oppgave)
        runBlocking {
            val oppgaveList = oppgaveService.getActiveOppgaver(user)
            val oppgaveDTO = oppgaveList.first()
            oppgaveDTO.tekst shouldBe "***"
            oppgaveDTO.link shouldBe "***"
            oppgaveDTO.sikkerhetsnivaa shouldBe 4
        }
    }

    @Test
    fun `should not mask events with security level lower than current user`() {
        var oppgave = createOppgave("1", "1", true)
        oppgave = oppgave.copy(sikkerhetsnivaa = 3)
        coEvery { oppgaveConsumer.getExternalActiveEvents(dummyToken) } returns listOf(oppgave)
        runBlocking {
            val oppgaveList = oppgaveService.getActiveOppgaver(user)
            val oppgaveDTO = oppgaveList.first()
            oppgaveDTO.tekst shouldBe oppgave.tekst
            oppgaveDTO.link shouldBe oppgave.link
            oppgaveDTO.sikkerhetsnivaa shouldBe 3
        }
    }

    @Test
    fun `should not mask events with security level equal than current user`() {
        val oppgave = createOppgave("1", "1", true)
        coEvery { oppgaveConsumer.getExternalActiveEvents(dummyToken) } returns listOf(oppgave)
        runBlocking {
            val oppgaveList = oppgaveService.getActiveOppgaver(user)
            val oppgaveDTO = oppgaveList.first()
            oppgaveDTO.tekst shouldBe oppgave.tekst
            oppgaveDTO.link shouldBe oppgave.link
            oppgaveDTO.sikkerhetsnivaa shouldBe 4
        }
    }

    @Test
    fun `should throw exception if fetching active events fails`() {
        coEvery { oppgaveConsumer.getExternalActiveEvents(dummyToken) } throws Exception("error")
        shouldThrow<ConsumeEventException> { runBlocking { oppgaveService.getActiveOppgaver(user) } }
    }

    @Test
    fun `should throw exception if fetching inactive events fails`() {
        coEvery { oppgaveConsumer.getExternalInactiveEvents(dummyToken) } throws Exception("error")
        shouldThrow<ConsumeEventException> { runBlocking { oppgaveService.getInactiveOppgaver(user) } }
    }

}
