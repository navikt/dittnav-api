package no.nav.personbruker.dittnav.api.digisos

import io.kotest.assertions.withClue
import io.kotest.matchers.shouldBe
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.defaultheaders.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.testing.*
import io.ktor.util.*
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.serialization.json.*
import no.nav.personbruker.dittnav.api.*
import no.nav.personbruker.dittnav.api.config.jsonConfig
import org.junit.jupiter.api.Test
import java.net.URL
import java.time.LocalDateTime

class DigisoApiTest {

    private val digisosTestHost = "https://digisos.test"
    private val mockkDigiSosTokendings = mockk<DigiSosTokendings>().also {
        coEvery { it.exchangeToken(any()) } returns "Access!"
    }

    private val paabegynteFromDigisos =
        listOf(
            createPaabegynte("123"),
            createPaabegynte("456", tekst = "annen"),
            createPaabegynte("789", tekst = "mer annen")
        )

    @Test
    fun `videreformidler 'done' til digisos`() = digisosTestApplication {
        val done = """{ "eventId": "eventId", "grupperingsId": "grupperingsId" }"""

        client.authenticatedPost(done, "dittnav-api/digisos/paabegynte/done").assert {
            status shouldBe HttpStatusCode.OK
        }
    }

    @Test
    fun `henter beskjeder fra digisos og presenterer dem som utkast`() = digisosTestApplication {
        client.authenticatedGet("dittnav-api/digisos/utkast").assert {
            status shouldBe HttpStatusCode.OK
            val resultArray = bodyAsText().toJsonArray()
            resultArray shouldHaveContentEqualTo paabegynteFromDigisos
        }
    }

    @Test
    fun `henter riktig antall 'utkast' fra digisos`() = digisosTestApplication {
        client.authenticatedGet("dittnav-api/digisos/utkast/antall").assert {
            status shouldBe HttpStatusCode.OK
            val result = bodyAsText().toJsonObject()
            result["antall"]?.jsonPrimitive?.int shouldBe 3
        }
    }


    @KtorDsl
    private fun digisosTestApplication(testBody: suspend ApplicationTestBuilder.() -> Unit) = testApplication {
        externalServices {
            hosts(digisosTestHost) {
                install(DefaultHeaders)
                install(ContentNegotiation) {
                    json(jsonConfig())
                }

                routing {
                    get("/dittnav/pabegynte/aktive") {
                        call.respond(
                            paabegynteFromDigisos
                        )
                    }

                    post("/dittnav/pabegynte/lest") {
                        call.respond(HttpStatusCode.OK)
                    }
                }
            }
        }

        mockApi(
            digiSosConsumer = DigiSosConsumer(
                client = applicationHttpClient(),
                digiSosSoknadBaseURL = URL(digisosTestHost),
                tokendings = mockkDigiSosTokendings
            )
        )

        testBody()
    }
}

private infix fun JsonArray.shouldHaveContentEqualTo(expected: List<Paabegynte>) {
    withClue("Feil antall elementer i liste") { size shouldBe expected.size }
    map { it.jsonObject }.forEach { json ->
        val utkastId = json["utkastId"]?.jsonPrimitive?.content
        val tittel = json["tittel"]?.jsonPrimitive?.content
        val link = json["link"]?.jsonPrimitive?.content
        val opprettet = json["opprettet"]?.jsonPrimitive?.content?.let { LocalDateTime.parse(it) }

        val paabegynte = expected.find { it.eventId == utkastId }!!


        paabegynte.eventId shouldBe utkastId
        paabegynte.tekst shouldBe tittel
        paabegynte.link shouldBe link
        paabegynte.eventTidspunkt shouldBe opprettet
    }
}
