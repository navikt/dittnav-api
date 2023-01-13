package no.nav.personbruker.dittnav.api.beskjed

import io.kotest.assertions.withClue
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.server.testing.testApplication
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import no.nav.personbruker.dittnav.api.applicationHttpClient
import no.nav.personbruker.dittnav.api.assert
import no.nav.personbruker.dittnav.api.authenticatedGet
import no.nav.personbruker.dittnav.api.bool
import no.nav.personbruker.dittnav.api.createActiveBeskjed
import no.nav.personbruker.dittnav.api.createInactiveBeskjed
import no.nav.personbruker.dittnav.api.digisos.DigiSosConsumer
import no.nav.personbruker.dittnav.api.digisos.DigiSosTokendings
import no.nav.personbruker.dittnav.api.mockApi
import no.nav.personbruker.dittnav.api.rawEventHandlerVarsel
import no.nav.personbruker.dittnav.api.respondRawJson
import no.nav.personbruker.dittnav.api.shouldBeSameDateTimeAs
import no.nav.personbruker.dittnav.api.string
import no.nav.personbruker.dittnav.api.stringArray
import no.nav.personbruker.dittnav.api.toJsonArray
import no.nav.personbruker.dittnav.api.toSpesificJsonFormat
import no.nav.personbruker.dittnav.api.tokenx.EventhandlerTokendings
import no.nav.personbruker.dittnav.api.zonedDateTime
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import java.net.URL
import java.time.ZonedDateTime


class BeskjedApiTest {
    private val now = ZonedDateTime.now()
    private val digisosTestHost = "https://digisos.test"
    private val eventhandlerTestHost = "https://digisos.test"
    private val mockkHandlerTokendings = mockk<EventhandlerTokendings>().also {
        coEvery { it.exchangeToken(any()) } returns "Access!"
    }
    private val mockkDigiSosTokendings = mockk<DigiSosTokendings>().also {
        coEvery { it.exchangeToken(any()) } returns "Access!"
    }

    private val expectedBeskjedFromDigsos =
        listOf(
            createActiveBeskjed("123", tekst = "Fordi variasjon er bra å ha").withDigiSosProdusent(),
            createActiveBeskjed("188").withDigiSosProdusent(),
            createInactiveBeskjed(
                eventId = "199",
                forstBehandlet = now.minusDays(30),
                sistOppdatert = now.minusDays(10),
            ).withDigiSosProdusent()
        )
    private val expectedBeskjedFromEventhandler = listOf(
        createInactiveBeskjed("665544"),
        createActiveBeskjed("8877", tekst = "Tekst er tekst er tekst").withEksternVarsling(listOf("SMS")),
        createActiveBeskjed(
            "8879",
            forstBehandlet = now.minusHours(1),
            sistOppdatert = now.minusMinutes(30),
        ),
        createActiveBeskjed("887765", tekst = "Her er testbeskjed med testtekst").withEksternVarsling()
    )

    @ParameterizedTest
    @CsvSource("true, dittnav-api/beskjed", "false, dittnav-api/beskjed/inaktiv")
    fun `beskjeder fra både eventhandler`(aktive: Boolean, endpoint: String) {
        val expectedBeskjedContent = expectedBeskjedFromEventhandler.filter { it.aktiv == aktive }

        testApplication {
            setupExternalBeskjedServices()
            mockApi(beskjedMergerService = createBeskjedMergerService())
            client.authenticatedGet(endpoint).assert {
                status shouldBe HttpStatusCode.OK
                val resultArray = bodyAsText().toJsonArray()
                resultArray shouldHaveContentEqualTo expectedBeskjedContent
            }
        }
    }

    private fun ApplicationTestBuilder.setupExternalBeskjedServices(
        withErrorFromDigiSos: Boolean = false,
        withErrorFromEventhandler: Boolean = false
    ) = externalServices {
        hosts(digisosTestHost, eventhandlerTestHost) {
            routing {
                get("/fetch/beskjed/aktive") {
                    call.customServiceResponse(
                        beskjeder = expectedBeskjedFromEventhandler.filter { it.aktiv },
                        withError = withErrorFromEventhandler,
                        jsonFormatFunction = Beskjed::toEventhandlerJson
                    )
                }
                get("/fetch/beskjed/inaktive") {
                    call.customServiceResponse(
                        beskjeder = expectedBeskjedFromEventhandler.filter { !it.aktiv },
                        withError = withErrorFromEventhandler,
                        jsonFormatFunction = Beskjed::toEventhandlerJson
                    )
                }
                get("/dittnav/pabegynte/aktive") {
                    call.customServiceResponse(
                        beskjeder = expectedBeskjedFromDigsos.filter { it.aktiv },
                        withError = withErrorFromDigiSos,
                        jsonFormatFunction = Beskjed::toPaabegynteDigisosJson
                    )
                }
                get("/dittnav/pabegynte/inaktive") {
                    call.customServiceResponse(
                        beskjeder = expectedBeskjedFromDigsos.filter { !it.aktiv },
                        withError = withErrorFromDigiSos,
                        jsonFormatFunction = Beskjed::toPaabegynteDigisosJson
                    )
                }
            }
        }
    }

    private fun ApplicationTestBuilder.createBeskjedMergerService(): BeskjedMergerService = BeskjedMergerService(
        beskjedConsumer = BeskjedConsumer(
            client = applicationHttpClient(),
            eventHandlerBaseURL = URL(eventhandlerTestHost),
            eventhandlerTokendings = mockkHandlerTokendings
        ),
        digiSosConsumer = DigiSosConsumer(
            client = applicationHttpClient(),
            digiSosSoknadBaseURL = URL(digisosTestHost),
            tokendings = mockkDigiSosTokendings
        )
    )
}

private suspend fun ApplicationCall.customServiceResponse(
    beskjeder: List<Beskjed>,
    withError: Boolean,
    jsonFormatFunction: Beskjed.() -> String
) {
    if (withError) {
        respond(HttpStatusCode.InternalServerError)
    } else {
        val aktiveJson = beskjeder.toSpesificJsonFormat(jsonFormatFunction)
        respondRawJson(aktiveJson)
    }
}

private infix fun JsonArray.shouldHaveContentEqualTo(expected: List<Beskjed>) {
    withClue("Feil antall elementer i liste") { size shouldBe expected.size }
    val resultObjects = this.map { it.jsonObject }
    expected.forEach { beskjedDTO -> resultObjects shouldContainBeskjedDTO beskjedDTO }
}


private infix fun List<JsonObject>.shouldContainBeskjedDTO(dto: Beskjed) =
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


private fun Beskjed.withEksternVarsling(kanaler: List<String> = listOf("SMS", "EPOST")): Beskjed =
    if (this.produsent == "digiSos") {
        throw IllegalArgumentException("Beskjeder fra digisos inneholder ikke eksterne varlinger")
    } else {
        this.copy(eksternVarslingKanaler = kanaler, eksternVarslingSendt = true)
    }

private fun Beskjed.withDigiSosProdusent(): Beskjed =
    if (eksternVarslingSendt) {
        throw IllegalArgumentException("Beskjeder fra digisos inneholder ikke eksterne varslinger")
    } else {
        this.copy(produsent = "digiSos")
    }


private fun Beskjed.toEventhandlerJson() = rawEventHandlerVarsel(
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

private fun Beskjed.toPaabegynteDigisosJson() = """
    {
      "eventTidspunkt": "${forstBehandlet.toLocalDateTime()}",
      "eventId": "$eventId",
      "grupperingsId": "$grupperingsId",
      "tekst": "$tekst",
      "link": "$link",
      "sikkerhetsnivaa": $sikkerhetsnivaa,
      "sistOppdatert": "${sistOppdatert.toLocalDateTime()}",
      "isAktiv": $aktiv
    }
""".trimIndent()

private fun Beskjed.testIdentifier(key: String): String = "eventId:${eventId}\tkey:$key"
