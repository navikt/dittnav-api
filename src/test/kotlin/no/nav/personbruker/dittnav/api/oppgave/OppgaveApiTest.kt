package no.nav.personbruker.dittnav.api.oppgave;

import io.kotest.assertions.withClue
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.server.testing.testApplication
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import no.nav.personbruker.dittnav.api.applicationHttpClient
import no.nav.personbruker.dittnav.api.authenticatedGet
import no.nav.personbruker.dittnav.api.bool
import no.nav.personbruker.dittnav.api.externalServiceWith500Response
import no.nav.personbruker.dittnav.api.mockApi
import no.nav.personbruker.dittnav.api.rawEventHandlerVarsel
import no.nav.personbruker.dittnav.api.externalServiceWithJsonResponse
import no.nav.personbruker.dittnav.api.shouldBeSameDateTimeAs
import no.nav.personbruker.dittnav.api.string
import no.nav.personbruker.dittnav.api.stringArray
import no.nav.personbruker.dittnav.api.toJsonArray
import no.nav.personbruker.dittnav.api.toSpesificJsonFormat
import no.nav.personbruker.dittnav.api.tokenx.AccessToken
import no.nav.personbruker.dittnav.api.tokenx.EventhandlerTokendings
import no.nav.personbruker.dittnav.api.zonedDateTime
import org.junit.jupiter.api.Test
import java.net.URL


class OppgaveApiTest {

    private val eventhandlerTestHost = "https://digisos.test"
    private val mockkTokendings = mockk<EventhandlerTokendings>().also {
        coEvery { it.exchangeToken(any()) } returns AccessToken("Access!")
    }
    private val oppgaverFromEventhandler = listOf(
        createInactiveOppgaveDTO("evhASAF"),
        createInactiveOppgaveDTO("hjjfkfa"),
        createInactiveOppgaveDTO("hja-da"),
        createInactiveOppgaveDTO("hjaanflska"),
        createActiveOppgave("hlaksjasølkjh"),
        createActiveOppgave("kajljflsf")
    )

    @Test
    fun `aktive oppgaver`() = testApplication {
        val expectedOppgaver = oppgaverFromEventhandler.filter { it.aktiv }
        mockApi(oppgaveService = createoppgaveService())
        externalServiceWithJsonResponse(
            hostApiBase = eventhandlerTestHost,
            endpoint = "/fetch/oppgave/aktive",
            content = expectedOppgaver.filter { it.aktiv }.toSpesificJsonFormat(OppgaveDTO::toEventhandlerJson)
        )

        client.authenticatedGet("dittnav-api/oppgave").apply {
            status shouldBe HttpStatusCode.OK
            val resultArray = bodyAsText().toJsonArray()
            resultArray shouldHaveContentEqualTo expectedOppgaver.filter { it.aktiv }
        }
    }

    @Test
    fun `inaktive oppgaver`() = testApplication {
        val expectedOppgaver = oppgaverFromEventhandler.filter { !it.aktiv }
        mockApi(oppgaveService = createoppgaveService())
        externalServiceWithJsonResponse(
            hostApiBase = eventhandlerTestHost,
            endpoint = "/fetch/oppgave/inaktive",
            content = expectedOppgaver.filter { !it.aktiv }.toSpesificJsonFormat(OppgaveDTO::toEventhandlerJson)
        )

        client.authenticatedGet("dittnav-api/oppgave/inaktiv").apply {
            status shouldBe HttpStatusCode.OK
            val resultArray = bodyAsText().toJsonArray()
            resultArray shouldHaveContentEqualTo expectedOppgaver.filter { !it.aktiv }
        }
    }

    @Test
    fun `503 når dittnav-api feiler mot eventhandlerApi`() = testApplication {
        mockApi(oppgaveService = createoppgaveService())
        externalServiceWith500Response(testHost = eventhandlerTestHost, route = "dittnav-api/oppgave/inaktiv")
        client.authenticatedGet("dittnav-api/oppgave/inaktiv").status shouldBe HttpStatusCode.ServiceUnavailable
    }

    private fun ApplicationTestBuilder.createoppgaveService(): OppgaveService =
        OppgaveService(
            oppgaveConsumer = OppgaveConsumer(
                client = applicationHttpClient(),
                eventHandlerBaseURL = URL(eventhandlerTestHost)
            ), eventhandlerTokendings = mockkTokendings
        )
}

private infix fun JsonArray.shouldHaveContentEqualTo(expected: List<OppgaveDTO>) {
    withClue("Feil antall elementer i liste") { size shouldBe expected.size }
    val resultObjects = this.map { it.jsonObject }
    expected.forEach { oppgaveDTO -> resultObjects shouldContainOppgaveDTO oppgaveDTO }
}

private infix fun List<JsonObject>.shouldContainOppgaveDTO(dto: OppgaveDTO) {
    this.find { it.string("eventId") == dto.eventId }?.let { jsonObject ->
        withClue(dto.testIdentifier("aktiv")) { jsonObject.bool("aktiv") shouldBe dto.aktiv }
        withClue(dto.testIdentifier("forstBehandlet")) { jsonObject.zonedDateTime("forstBehandlet") shouldBeSameDateTimeAs dto.forstBehandlet }
        withClue(dto.testIdentifier("sistOppdatert")) { jsonObject.zonedDateTime("sistOppdatert") shouldBeSameDateTimeAs dto.sistOppdatert }
        withClue(dto.testIdentifier("produsen")) { jsonObject.string("produsent") shouldBe dto.produsent }
        withClue(dto.testIdentifier("link")) { jsonObject.string("link") shouldBe dto.link }
        withClue(dto.testIdentifier("aktiv")) { jsonObject.string("tekst") shouldBe dto.tekst }
        withClue(dto.testIdentifier("eksternVarslingSendt")) { jsonObject.bool("eksternVarslingSendt") shouldBe dto.eksternVarslingSendt }
        withClue(dto.testIdentifier("eksternVarslingKanaler")) {
            jsonObject.stringArray("eksternVarslingKanaler") shouldContainExactly dto.eksternVarslingKanaler
        }
    } ?: throw AssertionError("Fant ikke beskjed med eventId ${dto.eventId}")
}

private fun OppgaveDTO.toEventhandlerJson() = rawEventHandlerVarsel(
    eventId = eventId,
    grupperingsId = grupperingsId,
    førstBehandlet = "$forstBehandlet",
    produsent = produsent,
    sikkerhetsnivå = 0,
    sistOppdatert = "$sistOppdatert",
    tekst = tekst,
    link = link,
    eksternVarslingSendt = eksternVarslingSendt,
    eksternVarslingKanaler = eksternVarslingKanaler,
    aktiv = aktiv
)

private fun createInactiveOppgaveDTO(eventId: String) =
    createOppgaveDTO(eventId = eventId, aktiv = false)

private fun createActiveOppgave(eventId: String) =
    createOppgaveDTO(eventId = eventId, aktiv = true)

private fun OppgaveDTO.testIdentifier(key: String): String = "eventId:${eventId}\tkey:$key"