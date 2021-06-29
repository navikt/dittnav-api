package no.nav.personbruker.dittnav.api.common

import io.ktor.http.*
import no.nav.personbruker.dittnav.api.beskjed.BeskjedDTO
import no.nav.personbruker.dittnav.api.beskjed.KildeType
import org.amshove.kluent.*
import org.junit.jupiter.api.Test

internal class MultiSourceResultTest {

    @Test
    fun `Skal summere to vellykkede resultater til et nytt resultatobjekt`() {
        val expectedSource1 = KildeType.EVENTHANDLER
        val expectedSource2 = KildeType.VARSELINNBOKS

        val source1 = MultiSourceResultObjectMother.giveMeNumberOfSuccessEventsForSource(1, expectedSource1, baseEventId = "beskjed")
        val source2 = MultiSourceResultObjectMother.giveMeNumberOfSuccessEventsForSource(2, expectedSource2, baseEventId = "varsel")

        val sum = source1 + source2

        sum.shouldNotBeNull()
        sum.results().size `should be equal to` (source1.results().size + source2.results().size)
        sum.results() `should contain all` source1.results()
        sum.results() `should contain all` source2.results()

        sum.failedSources().size `should be equal to` (source1.failedSources().size + source2.failedSources().size)

        sum.successFullSources().size `should be equal to` 2
        sum.successFullSources() `should contain all` listOf(expectedSource1, expectedSource2)

        sum.hasErrors() `should be equal to` false
        sum.determineHttpCode() `should be equal to` HttpStatusCode.OK
    }

    @Test
    fun `Skal summere et vellykket og et feilende resultat til et nytt resultatobjekt`() {
        val expectedSource1 = KildeType.EVENTHANDLER
        val expectedSource2 = KildeType.VARSELINNBOKS

        val source1 = MultiSourceResultObjectMother.giveMeNumberOfSuccessEventsForSource(1, expectedSource1)
        val source2 = MultiSourceResult.createErrorResult<BeskjedDTO, KildeType>(expectedSource2)

        val sum = source1 + source2

        sum.shouldNotBeNull()
        sum.results().size `should be equal to` (source1.results().size + source2.results().size)
        sum.results() `should contain all` source1.results()
        sum.results() `should contain all` source2.results()

        sum.failedSources().size `should be equal to` (source1.failedSources().size + source2.failedSources().size)

        sum.successFullSources().size `should be equal to` 1
        sum.successFullSources() `should contain` expectedSource1

        sum.hasErrors() `should be equal to` true
        sum.determineHttpCode() `should be equal to` HttpStatusCode.PartialContent
    }

    @Test
    fun `Skal summere to feilede resultater til et nytt resultatobjekt`() {
        val expectedSource1 = KildeType.EVENTHANDLER
        val expectedSource2 = KildeType.VARSELINNBOKS

        val source1 = MultiSourceResult.createErrorResult<BeskjedDTO, KildeType>(expectedSource1)
        val source2 = MultiSourceResult.createErrorResult<BeskjedDTO, KildeType>(expectedSource2)

        val tilsammen = source1 + source2

        tilsammen.shouldNotBeNull()
        tilsammen.results().size `should be equal to` (source1.results().size + source2.results().size)
        tilsammen.results().`should be empty`()

        tilsammen.failedSources().size `should be equal to` (source1.failedSources().size + source2.failedSources().size)

        tilsammen.successFullSources().size `should be equal to` 0

        tilsammen.hasErrors() `should be equal to` true
        tilsammen.failedSources() `should contain all` listOf(expectedSource1, expectedSource2)
        tilsammen.determineHttpCode() `should be equal to` HttpStatusCode.ServiceUnavailable
    }

    @Test
    fun `Skal kunne addere et tomt resultatobjekt til et annet resultat uten at resultatet endrer seg`() {
        val expectedSource1 = KildeType.EVENTHANDLER

        val validResult = MultiSourceResultObjectMother.giveMeNumberOfSuccessEventsForSource(1, expectedSource1)
        val emptyResult = MultiSourceResult.createEmptyResult()

        val sum = validResult + emptyResult

        sum.shouldNotBeNull()
        sum.results().size `should be equal to` validResult.results().size
        sum.results() `should contain all` validResult.results()

        sum.failedSources().size `should be equal to` validResult.failedSources().size

        sum.successFullSources().size `should be equal to` validResult.successFullSources().size
        sum.successFullSources() `should contain` expectedSource1

        sum.hasErrors() `should be equal to` false
        sum.determineHttpCode() `should be equal to` HttpStatusCode.OK
    }

}
