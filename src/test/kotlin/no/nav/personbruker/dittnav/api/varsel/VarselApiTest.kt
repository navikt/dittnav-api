package no.nav.personbruker.dittnav.api.varsel

import io.kotest.matchers.shouldBe
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.server.testing.testApplication
import io.mockk.coEvery
import io.mockk.mockk
import no.nav.personbruker.dittnav.api.applicationHttpClient
import no.nav.personbruker.dittnav.api.authenticatedGet
import no.nav.personbruker.dittnav.api.beskjed.BeskjedMergerService
import no.nav.personbruker.dittnav.api.beskjed.createActiveBeskjedDto
import no.nav.personbruker.dittnav.api.common.MultiSourceResult
import no.nav.personbruker.dittnav.api.externalServiceWith500Response
import no.nav.personbruker.dittnav.api.innboks.InnboksService
import no.nav.personbruker.dittnav.api.innboks.createInnboks
import no.nav.personbruker.dittnav.api.innboks.toInnboksDTO
import no.nav.personbruker.dittnav.api.int
import no.nav.personbruker.dittnav.api.mockApi
import no.nav.personbruker.dittnav.api.oppgave.OppgaveConsumer
import no.nav.personbruker.dittnav.api.oppgave.OppgaveService
import no.nav.personbruker.dittnav.api.oppgave.createOppgave
import no.nav.personbruker.dittnav.api.toJsonObject
import no.nav.personbruker.dittnav.api.tokenx.AccessToken
import no.nav.personbruker.dittnav.api.tokenx.EventhandlerTokendings
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.net.URL

class VarselApiTest {
    private val eventhandlerTestHost = "https://testtest.test"
    private val mockkTokendings = mockk<EventhandlerTokendings>().also {
        coEvery { it.exchangeToken(any()) } returns AccessToken("Access!")
    }

    private val beskjeder = listOf(
        createActiveBeskjedDto("jsdhfs"),
        createActiveBeskjedDto("vjshf"),
        createActiveBeskjedDto("vijrsr")
    )

    private val oppgaver = listOf(
        createOppgave("hlaksjasølkjh", aktiv = true).toOppgaveDTO(),
        createOppgave("kajljflsf", aktiv = true).toOppgaveDTO()
    )

    private val innbokser = listOf(
        toInnboksDTO(createInnboks("bervj", aktiv = true)),
    )

    private val beskjedMergerService: BeskjedMergerService = mockk()
    private val oppgaveService: OppgaveService = mockk()
    private val innboksService: InnboksService = mockk()

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
            oppgaveService = oppgaveService,
            innboksService = innboksService
        )

        client.authenticatedGet("dittnav-api/varsel/antall").apply {
            status shouldBe HttpStatusCode.OK
            val result = bodyAsText().toJsonObject()
            result.int("beskjeder") shouldBe 3
            result.int("oppgaver") shouldBe 2
            result.int("innbokser") shouldBe 1
        }
    }

    @Test
    fun `503 når dittnav-api feiler mot eventhandlerApi`() = testApplication {
        mockApi(
            beskjedMergerService = beskjedMergerService,
            oppgaveService = createoppgaveService(),
            innboksService = innboksService
        )
        externalServiceWith500Response(testHost = eventhandlerTestHost, route = "dittnav-api/oppgave/inaktiv")
        client.authenticatedGet("dittnav-api/varsel/antall").status shouldBe HttpStatusCode.ServiceUnavailable
    }

    private fun ApplicationTestBuilder.createoppgaveService(): OppgaveService =
        OppgaveService(
            oppgaveConsumer = OppgaveConsumer(
                client = applicationHttpClient(),
                eventHandlerBaseURL = URL(eventhandlerTestHost)
            ), eventhandlerTokendings = mockkTokendings
        )
}