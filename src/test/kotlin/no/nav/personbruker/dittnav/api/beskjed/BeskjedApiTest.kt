package no.nav.personbruker.dittnav.api.beskjed;

import io.kotest.assertions.withClue
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.server.testing.testApplication
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import no.finn.unleash.FakeUnleash
import no.nav.personbruker.dittnav.api.applicationHttpClient
import no.nav.personbruker.dittnav.api.authenticatedGet
import no.nav.personbruker.dittnav.api.bool
import no.nav.personbruker.dittnav.api.digisos.DigiSosClient
import no.nav.personbruker.dittnav.api.digisos.DigiSosService
import no.nav.personbruker.dittnav.api.mockApi
import no.nav.personbruker.dittnav.api.rawEventHandlerVarsel
import no.nav.personbruker.dittnav.api.respondRawJson
import no.nav.personbruker.dittnav.api.string
import no.nav.personbruker.dittnav.api.stringArray
import no.nav.personbruker.dittnav.api.toSpesificJsonFormat
import no.nav.personbruker.dittnav.api.tokenx.AccessToken
import no.nav.personbruker.dittnav.api.tokenx.EventhandlerTokendings
import no.nav.personbruker.dittnav.api.unleash.UnleashService
import no.nav.personbruker.dittnav.api.zonedDateTime
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.net.URL
import java.time.ZonedDateTime


class BeskjedApiTest {
    private val now = ZonedDateTime.now()
    private val digisosTestHost = "https://digisos.test"
    private val eventhandlerTestHost = "https://digisos.test"
    private val mockkTokendings = mockk<EventhandlerTokendings>().also {
        coEvery { it.exchangeToken(any()) } returns AccessToken("Access!")
    }
    private val fakeUnleash = FakeUnleash()

    private val expectedBeskjedFromDigsos =
        listOf(
            createActiveBeskjedDto("123", tekst = "Fordi variasjon er bra å ha").withDigiSosProdusent(),
            createActiveBeskjedDto("188").withDigiSosProdusent(),
            createInactiveBeskjedDto(
                eventId = "199",
                forstBehandlet = now.minusDays(30),
                sistOppdatert = now.minusDays(10),
            ).withDigiSosProdusent()
        )
    private val expectedBeskjedFromEventhandler = listOf(
        createInactiveBeskjedDto("665544"),
        createActiveBeskjedDto("8877", tekst = "Tekst er tekst er tekst").withEksternVarsling(listOf("SMS")),
        createActiveBeskjedDto(
            "8879",
            forstBehandlet = now.minusHours(1),
            sistOppdatert = now.minusMinutes(30),
        ),
        createActiveBeskjedDto("887765", tekst = "Her er testbeskjed med testtekst").withEksternVarsling()
    )

    @BeforeEach
    fun clearUnleash() {
        fakeUnleash.resetAll()
    }

    @Test
    fun `henter beskjeder fra digisos og eventhandler`() {
        fakeUnleash.enable(UnleashService.digisosPaabegynteToggleName)
        val expectedBeskjedDTOs =
            expectedBeskjedFromDigsos.filter { it.aktiv } + expectedBeskjedFromEventhandler.filter { it.aktiv }
        testApplication {
            setupExternalBeskjedServices()
            mockApi(beskjedMergerService = createBeskjedMergerService())

            client.authenticatedGet("dittnav-api/beskjed").apply {
                status shouldBe HttpStatusCode.OK
                val resultArray = Json.parseToJsonElement(bodyAsText()).jsonArray
                resultArray shouldHaveContentEqualTo expectedBeskjedDTOs
            }
        }
    }

    @Test
    fun `henter beskjeder fra eventhandler`() {

        testApplication {
            val beskjedOject =

                client.get("/beskjed").apply {
                    //TODO("Please write your test here")
                }
        }
    }

    @Test
    fun testGetBeskjedInaktiv() = testApplication {

        client.get("/beskjed/inaktiv").apply {
            //TODO("Please write your test here")
        }
    }


    private fun ApplicationTestBuilder.setupExternalBeskjedServices() =
        externalServices {
            hosts(digisosTestHost, eventhandlerTestHost) {
                routing {
                    get("/fetch/beskjed/aktive") {
                        val aktiveJson = expectedBeskjedFromEventhandler.filter { it.aktiv }
                            .toSpesificJsonFormat(BeskjedDTO::toEventhandlerJson)
                        call.respondRawJson(aktiveJson)
                    }
                    get("/fetch/beskjed/inaktive") {
                        val inaktiveJson = expectedBeskjedFromEventhandler.filter { !it.aktiv }
                            .toSpesificJsonFormat(BeskjedDTO::toEventhandlerJson)
                        call.respondRawJson(inaktiveJson)
                    }
                    get("/dittnav/pabegynte/aktive") {
                        val aktiveJson = expectedBeskjedFromDigsos.filter { it.aktiv }
                            .toSpesificJsonFormat(formatter = BeskjedDTO::toPaabegynteDigisosJson)
                        call.respondRawJson(aktiveJson)
                    }
                    get("/dittnav/pabegynte/inaktive") {
                        val inaktiveJson = expectedBeskjedFromDigsos.filter { !it.aktiv }
                            .toSpesificJsonFormat(formatter = BeskjedDTO::toEventhandlerJson)
                        call.respondRawJson(inaktiveJson)
                    }
                }
            }
        }

    private fun ApplicationTestBuilder.createBeskjedMergerService(): BeskjedMergerService = BeskjedMergerService(
        beskjedService = BeskjedService(
            beskjedConsumer = BeskjedConsumer(
                client = applicationHttpClient(),
                eventHandlerBaseURL = URL(eventhandlerTestHost)
            ),
            eventhandlerTokendings = mockkTokendings
        ),
        digiSosService = DigiSosService(
            digiSosClient = DigiSosClient(
                client = applicationHttpClient(),
                digiSosSoknadBaseURL = URL(digisosTestHost)
            )
        ),
        unleashService = UnleashService(unleashClient = fakeUnleash)
    )

}

private infix fun JsonArray.shouldHaveContentEqualTo(expected: List<BeskjedDTO>) {
    withClue("Feil antall elementer i liste") { size shouldBe expected.size }
    val resultObjects = this.map { it.jsonObject }
    expected.forEach { beskjedDTO -> resultObjects shouldContainBeskjedDTO beskjedDTO }
}


private infix fun List<JsonObject>.shouldContainBeskjedDTO(dto: BeskjedDTO) =
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

private infix fun ZonedDateTime.shouldBeSameDateTimeAs(expected: ZonedDateTime) =
    this.toLocalDateTime() shouldBe expected.toLocalDateTime()


private fun BeskjedDTO.withEksternVarsling(kanaler: List<String> = listOf("SMS", "EPOST")): BeskjedDTO =
    if (this.produsent == "digiSos") {
        throw IllegalArgumentException("Beskjeder fra digisos inneholder ikke eksterne varlinger")
    } else {
        this.copy(eksternVarslingKanaler = kanaler, eksternVarslingSendt = true)
    }

private fun BeskjedDTO.withDigiSosProdusent(): BeskjedDTO =
    if (eksternVarslingSendt) {
        throw IllegalArgumentException("Beskjeder fra digisos inneholder ikke eksterne varlinger")
    } else {
        this.copy(produsent = "digiSos")
    }


private fun BeskjedDTO.toEventhandlerJson() = rawEventHandlerVarsel(
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

private fun BeskjedDTO.toPaabegynteDigisosJson() = """
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
private fun BeskjedDTO.testIdentifier(key: String): String = "eventId:${eventId}\tkey:$key"