package no.nav.personbruker.dittnav.api.saksoversikt

import org.amshove.kluent.`should be equal to`
import org.junit.jupiter.api.Test
import java.time.LocalDate

internal class SakstemaTransformerTest {

    @Test
    fun `should transform external sakstema`() {
        val kode = "temakode"
        val navn = "temanavn"
        val antall = 3
        val sistOppdatert = LocalDate.now()

        val external = SakstemaExternalObjectMother.createSakstema(kode, navn, antall, sistOppdatert)

        val result = SakstemaTransformer.toInternal(listOf(external))
        val firstInWrapper = result.sakstemaList.first()

        result.antallSakstema `should be equal to` 1
        firstInWrapper.temakode `should be equal to` kode
        firstInWrapper.temanavn `should be equal to` navn
        firstInWrapper.antallStatusUnderBehandling `should be equal to` antall
        firstInWrapper.sisteOppdatering `should be equal to` sistOppdatert
    }

    @Test
    fun `should sort sakstema by sisteOppdatering descending`() {
        val date1 = LocalDate.now()
        val date2 = LocalDate.now().minusDays(3)
        val date3 = LocalDate.now().minusDays(5)
        val date4 = LocalDate.now().plusDays(3)
        val date5 = LocalDate.now().plusDays(5)

        val external = SakstemaExternalObjectMother.createSakstemaWithSistoppdatert(date1, date2, date3, date4, date5)

        val result = SakstemaTransformer.toInternal(external)

        result.antallSakstema `should be equal to` 5
        result.sakstemaList `should be sorted according to descending` { sakstema -> sakstema.sisteOppdatering }
    }

    private infix fun <T, V: Comparable<V>> Collection<T>.`should be sorted according to descending`(field: (T) -> V) {
        val sorted = sortedByDescending(field)

        this `should be equal to` sorted
    }
}
