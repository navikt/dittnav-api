package no.nav.personbruker.dittnav.api.beskjed;

import io.ktor.client.request.get
import io.ktor.server.application.call
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.server.testing.testApplication
import no.nav.personbruker.dittnav.api.rawEventHandlerVarsel
import no.nav.personbruker.dittnav.api.respondRawJson
import no.nav.personbruker.dittnav.api.toSpesificJsonFormat
import org.junit.jupiter.api.Test
import java.time.ZonedDateTime
import kotlin.text.StringBuilder


class BeskjedApiTest {
    private val now = ZonedDateTime.now()
    private val digisosTestHost = "https://digisos.test"
    private val eventhandlerTestHost = "https://digisos.test"

    private val expectedBeskjedFromDigsos =
        listOf(
            createActiveBeskjedDto("123", tekst = "Fordi variasjon er bra å ha"),
            createActiveBeskjedDto("188").withEksternVarsling(),
            createInactiveBeskjedDto(
                eventId = "188",
                forstBehandlet = now.minusDays(30),
                sistOppdatert = now.minusDays(10)
            ).withEksternVarsling()
        )
    private val expectedBeskjedFromEventhandler = listOf(
        createInactiveBeskjedDto("665544"),
        createActiveBeskjedDto("8877", tekst = "Tekst er tekst er tekst").withEksternVarsling(listOf("SMS")),
        createActiveBeskjedDto(
            "8879",
            forstBehandlet = now.minusHours(1),
            sistOppdatert = now.minusMinutes(30)
        ),
        createActiveBeskjedDto("887765", tekst = "Her er testbeskjed med testtekst").withEksternVarsling()
    )

    @Test
    fun `henter beskjeder fra digisos og eventhandler`() {
        testApplication {
            setupExternalBeskjedServices()

            client.get("/beskjed").apply {
                //TODO("Please write your test here")
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

private fun BeskjedDTO.withEksternVarsling(kanaler: List<String> = listOf("SMS", "EPOST")) =
    this.copy(eksternVarslingKanaler = kanaler, eksternVarslingSendt = true)