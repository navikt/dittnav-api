package no.nav.personbruker.dittnav.api.innboks

import io.kotest.matchers.shouldBe
import io.ktor.server.testing.testApplication
import kotlinx.coroutines.runBlocking
import no.nav.personbruker.dittnav.api.rawEventHandlerVarsel
import no.nav.personbruker.dittnav.api.tokenx.AccessToken
import no.nav.personbruker.dittnav.api.applicationHttpClient
import no.nav.personbruker.dittnav.api.oppgave.Oppgave
import no.nav.personbruker.dittnav.api.setupExternalServiceWithJsonResponse
import no.nav.personbruker.dittnav.api.toSpesificJsonFormat
import org.junit.jupiter.api.Test
import java.net.URL

internal class InnboksConsumerTest {

    private val dummyToken = AccessToken("<access_token>")
    private val testEventHandlerEndpoint = "http://event-handler"

    @Test
    fun `should get list of active Innboks`() {
        val innboksObject1 = createInnboks("1", "1", true)
        val innboksObject2 = createInnboks("2", "2", true)
        testApplication {

            setupExternalServiceWithJsonResponse(
                hostApiBase = testEventHandlerEndpoint,
                endpoint = "/fetch/innboks/aktive",
                content = listOf(innboksObject1, innboksObject2).toSpesificJsonFormat(Innboks::toEventHandlerJson)
            )
            val innboksConsumer = InnboksConsumer(applicationHttpClient(), URL(testEventHandlerEndpoint))

            runBlocking {
                val externalActiveEvents = innboksConsumer.getExternalActiveEvents(dummyToken)
                externalActiveEvents.size shouldBe 2
                externalActiveEvents shouldContainInnboksObject innboksObject1
                externalActiveEvents shouldContainInnboksObject innboksObject2
            }
        }
    }

    @Test
    fun `should get list of inactive Innboks`() {
        val innboksObject1 = createInnboks("1", "1", false)
        val innboksObject2 = createInnboks("5", "1", false)
        val innboksObject3 = createInnboks("6", "22", false)

        testApplication {
            val innboksConsumer = InnboksConsumer(applicationHttpClient(), URL(testEventHandlerEndpoint))
            setupExternalServiceWithJsonResponse(
                hostApiBase = testEventHandlerEndpoint,
                endpoint = "/fetch/innboks/inaktive",
                content = listOf(
                    innboksObject1,
                    innboksObject2,
                    innboksObject3
                ).toSpesificJsonFormat(Innboks::toEventHandlerJson)
            )

            runBlocking {
                val externalInactiveEvents = innboksConsumer.getExternalInactiveEvents(dummyToken)
                externalInactiveEvents shouldContainInnboksObject innboksObject1
                externalInactiveEvents shouldContainInnboksObject innboksObject2
                externalInactiveEvents shouldContainInnboksObject innboksObject3
            }
        }
    }
}

private infix fun List<Innboks>.shouldContainInnboksObject(expectedInnboks: Innboks) =
    find { it.eventId == expectedInnboks.eventId }?.let { event ->
        event.tekst shouldBe expectedInnboks.tekst
        event.fodselsnummer shouldBe expectedInnboks.fodselsnummer
        event.aktiv shouldBe expectedInnboks.aktiv
    } ?: throw AssertionError("Fant ikke innboksvarsel med eventid ${expectedInnboks.eventId}")

private fun Innboks.toEventHandlerJson(): String = rawEventHandlerVarsel(
    eventId = eventId,
    fodselsnummer = fodselsnummer,
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