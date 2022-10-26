package no.nav.personbruker.dittnav.api.common

import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.ktor.http.HttpStatusCode
import no.nav.personbruker.dittnav.api.beskjed.BeskjedDTO
import no.nav.personbruker.dittnav.api.beskjed.KildeType
import no.nav.personbruker.dittnav.api.createSuccsessfullMultiSourceResult
import no.nav.personbruker.dittnav.api.oppgave.OppgaveDTO
import org.junit.jupiter.api.Test
import java.time.ZonedDateTime

internal class MultiSourceResultTest {

    @Test
    fun `Skal summere to vellykkede resultater til et nytt resultatobjekt`() {
        val expectedSource1 = KildeType.EVENTHANDLER
        val expectedSource2 = KildeType.DIGISOS

        val source1 = createSuccsessfullMultiSourceResult(1, expectedSource1, baseEventId = "beskjed")
        val source2 = createSuccsessfullMultiSourceResult(2, expectedSource2, baseEventId = "digisos")

        val sum = source1 + source2

        sum.shouldNotBeNull()
        sum.results().size shouldBe (source1.results().size + source2.results().size)
        sum.results() shouldContainAll source1.results()
        sum.results() shouldContainAll source2.results()

        sum.failedSources().size shouldBe (source1.failedSources().size + source2.failedSources().size)

        sum.successFullSources().size shouldBe 2
        sum.successFullSources() shouldBe listOf(expectedSource1, expectedSource2)

        sum.hasErrors() shouldBe false
        sum.determineHttpCode() shouldBe HttpStatusCode.OK
    }

    @Test
    fun `Skal summere et vellykket og et feilende resultat til et nytt resultatobjekt`() {
        val expectedSource1 = KildeType.EVENTHANDLER
        val expectedSource2 = KildeType.DIGISOS

        val source1 = createSuccsessfullMultiSourceResult(1, expectedSource1)
        val source2 = MultiSourceResult.createErrorResult<BeskjedDTO, KildeType>(expectedSource2)

        val sum = source1 + source2

        sum.shouldNotBeNull()
        sum.results().size shouldBe (source1.results().size + source2.results().size)
        sum.results() shouldContainAll  source1.results()
        sum.results() shouldContainAll source2.results()

        sum.failedSources().size shouldBe (source1.failedSources().size + source2.failedSources().size)

        sum.successFullSources().size shouldBe 1
        sum.successFullSources() shouldContain expectedSource1

        sum.hasErrors() shouldBe true
        sum.determineHttpCode() shouldBe HttpStatusCode.PartialContent
    }

    @Test
    fun `Skal summere to feilede resultater til et nytt resultatobjekt`() {
        val expectedSource1 = KildeType.EVENTHANDLER
        val expectedSource2 = KildeType.DIGISOS

        val source1 = MultiSourceResult.createErrorResult<BeskjedDTO, KildeType>(expectedSource1)
        val source2 = MultiSourceResult.createErrorResult<BeskjedDTO, KildeType>(expectedSource2)

        val tilsammen = source1 + source2

        tilsammen.shouldNotBeNull()
        tilsammen.results().size shouldBe (source1.results().size + source2.results().size)
        tilsammen.results().shouldBeEmpty()

        tilsammen.failedSources().size shouldBe (source1.failedSources().size + source2.failedSources().size)

        tilsammen.successFullSources().size shouldBe 0

        tilsammen.hasErrors() shouldBe true
        tilsammen.failedSources() shouldBe listOf(expectedSource1, expectedSource2)
        tilsammen.determineHttpCode() shouldBe HttpStatusCode.ServiceUnavailable
    }

    @Test
    fun `Skal kunne addere et tomt resultatobjekt for beskjed til et annet resultat uten at resultatet endrer seg`() {
        val expectedSource1 = KildeType.EVENTHANDLER

        val validResult = createSuccsessfullMultiSourceResult(1, expectedSource1)
        val emptyBeskjedResult = MultiSourceResult.createEmptyResult<BeskjedDTO>()

        val sum = validResult + emptyBeskjedResult

        sum.shouldNotBeNull()
        sum.results().size shouldBe validResult.results().size
        sum.results() shouldBe validResult.results()

        sum.failedSources().size shouldBe validResult.failedSources().size

        sum.successFullSources().size shouldBe validResult.successFullSources().size
        sum.successFullSources() shouldContain expectedSource1

        sum.hasErrors() shouldBe false
        sum.determineHttpCode() shouldBe HttpStatusCode.OK
    }

    @Test
    fun `Skal kunne addere et tomt resultatobjekt for oppgave til et annet resultat uten at resultatet endrer seg`() {
        val expectedSource1 = KildeType.EVENTHANDLER

        val validResult = createSuccessfulOppgaveEventsForSource(1, expectedSource1)
        val emptyBeskjedResult = MultiSourceResult.createEmptyResult<OppgaveDTO>()

        val sum = validResult + emptyBeskjedResult

        sum.shouldNotBeNull()
        sum.results().size shouldBe validResult.results().size
        sum.results() shouldBe validResult.results()

        sum.failedSources().size shouldBe validResult.failedSources().size

        sum.successFullSources().size shouldBe validResult.successFullSources().size
        sum.successFullSources() shouldContain expectedSource1

        sum.hasErrors() shouldBe false
        sum.determineHttpCode() shouldBe HttpStatusCode.OK
    }

}

private fun createSuccessfulOppgaveEventsForSource(
    numberOfEvents: Int,
    source: KildeType,
    baseEventId: String = "oppgave"
): MultiSourceResult<OppgaveDTO, KildeType> {
    val events = mutableListOf<OppgaveDTO>()
    for (lopenummer in 0 until numberOfEvents) {
        events.add(createActiveOppgave("$baseEventId-$lopenummer"))
    }
    return MultiSourceResult.createSuccessfulResult(
        events,
        source
    )
}
private fun createActiveOppgave(eventId: String): OppgaveDTO {
    return OppgaveDTO(
        forstBehandlet = ZonedDateTime.now(),
        eventId = eventId,
        tekst = "Dummytekst",
        link = "https://dummy.url",
        produsent = "dummy-produsent",
        sistOppdatert = ZonedDateTime.now().minusDays(3),
        sikkerhetsnivaa = 3,
        aktiv = true,
        grupperingsId = "321",
        eksternVarslingSendt = true,
        eksternVarslingKanaler = listOf("SMS", "EPOST")
    )
}