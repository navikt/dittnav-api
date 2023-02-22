package no.nav.personbruker.dittnav.api.varsel

import io.kotest.matchers.shouldBe
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.testApplication
import io.mockk.coEvery
import io.mockk.mockk
import no.nav.personbruker.dittnav.api.authenticatedGet
import no.nav.personbruker.dittnav.api.beskjed.BeskjedMergerService
import no.nav.personbruker.dittnav.api.common.MultiSourceResult
import no.nav.personbruker.dittnav.api.createBeskjed
import no.nav.personbruker.dittnav.api.innboks.InnboksConsumer
import no.nav.personbruker.dittnav.api.innboks.createInnboks
import no.nav.personbruker.dittnav.api.int
import no.nav.personbruker.dittnav.api.mockApi
import no.nav.personbruker.dittnav.api.oppgave.OppgaveConsumer
import no.nav.personbruker.dittnav.api.oppgave.createOppgave
import no.nav.personbruker.dittnav.api.toJsonObject
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class VarselApiTest {

    private val beskjeder = listOf(
        createBeskjed("jsdhfs", aktiv = true).toBeskjedDto(4),
        createBeskjed("vjshf", aktiv = true).toBeskjedDto(4),
        createBeskjed("vijrsr",  aktiv = true).toBeskjedDto(4)
    )

    private val oppgaver = listOf(
        createOppgave("hlaksjasølkjh", aktiv = true).toOppgaveDTO(4),
        createOppgave("kajljflsf", aktiv = true).toOppgaveDTO(4)
    )

    private val innbokser = listOf(
        createInnboks("bervj", aktiv = true).toInnboksDTO(4),
    )

    private val beskjedMergerService: BeskjedMergerService = mockk()
    private val oppgaveService: OppgaveConsumer = mockk()
    private val innboksService: InnboksConsumer = mockk()

    @BeforeEach
    fun setup() {
        coEvery {
            beskjedMergerService.getActiveEvents(any())
        } returns MultiSourceResult(beskjeder, emptyList(), emptyList())

        coEvery {
            oppgaveService.getActiveOppgaver(any())
        } returns oppgaver

        coEvery {
            innboksService.getActiveInnboksEvents(any())
        } returns innbokser
    }


    @Test
    fun `antall varsler`() = testApplication {

        mockApi(
            beskjedMergerService = beskjedMergerService,
            oppgaveConsumer = oppgaveService,
            innboksConsumer = innboksService
        )

        client.authenticatedGet("dittnav-api/varsel/antall").apply {
            status shouldBe HttpStatusCode.OK
            val result = bodyAsText().toJsonObject()
            result.int("beskjeder") shouldBe 3
            result.int("oppgaver") shouldBe 2
            result.int("innbokser") shouldBe 1
        }
    }
}