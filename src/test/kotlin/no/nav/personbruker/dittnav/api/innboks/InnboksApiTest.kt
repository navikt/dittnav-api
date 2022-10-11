package no.nav.personbruker.dittnav.api.innboks;

import io.kotest.assertions.withClue
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.server.testing.testApplication
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import no.nav.personbruker.dittnav.api.applicationHttpClient
import no.nav.personbruker.dittnav.api.authenticatedGet
import no.nav.personbruker.dittnav.api.bool
import no.nav.personbruker.dittnav.api.externalServiceWithJsonResponse
import no.nav.personbruker.dittnav.api.mockApi
import no.nav.personbruker.dittnav.api.shouldBeSameDateTimeAs
import no.nav.personbruker.dittnav.api.string
import no.nav.personbruker.dittnav.api.stringArray
import no.nav.personbruker.dittnav.api.toJsonArray
import no.nav.personbruker.dittnav.api.toSpesificJsonFormat
import no.nav.personbruker.dittnav.api.tokenx.AccessToken
import no.nav.personbruker.dittnav.api.tokenx.EventhandlerTokendings
import no.nav.personbruker.dittnav.api.zonedDateTime
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import java.net.URL


class InnboksApiTest {
    private val eventhandlerTestHost = "https://eventhandler.test"
    private val innboksFraEventhandler = listOf(
        createInactiveInnboksDTO("evhASAF"),
        createInactiveInnboksDTO("hjjfkfa"),
        createInactiveInnboksDTO("hja-da"),
        createInactiveInnboksDTO("hjaanflska"),
        createActiveInnboksDTO("hlaksjasølkjh"),
        createActiveInnboksDTO("kajljflsf")
    )

    @ParameterizedTest
    @CsvSource("true, /fetch/innboks/aktive, dittnav-api/innboks", "false, /fetch/innboks/inaktive, dittnav-api/innboks/inaktiv")
    fun `innboks-varsler`(expectedAktivStatus:Boolean, eventhandlerEndpoint:String, dittnavApiEndpoint: String) = testApplication {
        mockApi(innboksService = createInnboksService())
        externalServiceWithJsonResponse(
            eventhandlerTestHost,
            eventhandlerEndpoint,
            innboksFraEventhandler.filter { it.aktiv == expectedAktivStatus }.toSpesificJsonFormat(Innboks::toEventHandlerJson)
        )
        client.authenticatedGet(dittnavApiEndpoint).apply {
            status shouldBe HttpStatusCode.OK
            val resultArray = bodyAsText().toJsonArray()
            resultArray shouldHaveContentEqualTo innboksFraEventhandler.filter { it.aktiv == expectedAktivStatus }
        }
    }

    private fun ApplicationTestBuilder.createInnboksService(): InnboksService = InnboksService(
        innboksConsumer = InnboksConsumer(
            client = applicationHttpClient(),
            eventHandlerBaseURL = URL(eventhandlerTestHost)
        ),
        eventhandlerTokenDings = mockk<EventhandlerTokendings>().also {
            coEvery { it.exchangeToken(any()) } returns AccessToken("duumyToken")
        }
    )

}
private infix fun JsonArray.shouldHaveContentEqualTo(expected: List<Innboks>) {
    withClue("Feil antall elementer i liste") { size shouldBe expected.size }
    val resultObjects = this.map { it.jsonObject }
    expected.forEach { innboks -> resultObjects shouldContainInnboksDTO innboks }
}

private infix fun List<JsonObject>.shouldContainInnboksDTO(dto: Innboks) =
    this.find { it.string("eventId") == dto.eventId }?.let { jsonObject ->
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

private fun createInactiveInnboksDTO(eventId: String): Innboks = createInnboks(eventId = eventId, aktiv = false)

private fun createActiveInnboksDTO(eventId: String): Innboks = createInnboks(eventId, true)

private fun Innboks.testIdentifier(key: String): String = "eventId:${eventId}\tkey:$key"