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

        val tilsammen = source1 + source2

        tilsammen.shouldNotBeNull()
        tilsammen.results().size `should be equal to` (source1.results().size + source2.results().size)
        tilsammen.results() `should contain all` source1.results()
        tilsammen.results() `should contain all` source2.results()

        tilsammen.failedSources().size `should be equal to` (source1.failedSources().size + source2.failedSources().size)

        tilsammen.successFullSources().size `should be equal to` 2
        tilsammen.successFullSources() `should contain all` listOf(expectedSource1, expectedSource2)

        tilsammen.hasErrors() `should be equal to` false
        tilsammen.determineHttpCode() `should be equal to` HttpStatusCode.OK
    }

    @Test
    fun `Skal summere et vellykket og et feilende resultat til et nytt resultatobjekt`() {
        val expectedSource1 = KildeType.EVENTHANDLER
        val expectedSource2 = KildeType.VARSELINNBOKS

        val source1 = MultiSourceResultObjectMother.giveMeNumberOfSuccessEventsForSource(1, expectedSource1)
        val source2 = MultiSourceResult.createErrorResult<BeskjedDTO, KildeType>(expectedSource2)

        val tilsammen = source1 + source2

        tilsammen.shouldNotBeNull()
        tilsammen.results().size `should be equal to` (source1.results().size + source2.results().size)
        tilsammen.results() `should contain all` source1.results()
        tilsammen.results() `should contain all` source2.results()

        tilsammen.failedSources().size `should be equal to` (source1.failedSources().size + source2.failedSources().size)

        tilsammen.successFullSources().size `should be equal to` 1
        tilsammen.successFullSources() `should contain` expectedSource1

        tilsammen.hasErrors() `should be equal to` true
        tilsammen.determineHttpCode() `should be equal to` HttpStatusCode.PartialContent
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

}
