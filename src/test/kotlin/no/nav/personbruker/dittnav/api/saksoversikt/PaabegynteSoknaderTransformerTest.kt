package no.nav.personbruker.dittnav.api.saksoversikt

import org.amshove.kluent.`should be equal to`
import org.junit.jupiter.api.Test

internal class PaabegynteSoknaderTransformerTest {
    private val saksoversiktUrl = "http://saksoversikt"

    private val transformer = PaabegynteSoknaderTransformer(saksoversiktUrl)

    @Test
    fun `Should handle empty list of paabegynte-uris and baksystem`() {
        val external = PaabegynteSoknaderExternalObjectMother.createPaabegyntSoknad(emptyList(), emptyList())

        val result = transformer.toInternal(external)

        result.antallPaabegynte `should be equal to` 0
        result.url `should be equal to` null
        result.feilendeBaksystem.`should be empty`()
    }

    @Test
    fun `Should use specific url when there is only one paabegynte-uri`() {
        val paagebynteUri = "http://paabegynt"

        val external = PaabegynteSoknaderExternalObjectMother.createPaabegyntSoknad(listOf(paagebynteUri), emptyList())

        val result = transformer.toInternal(external)

        result.antallPaabegynte `should be equal to` 1
        result.url `should be equal to` paagebynteUri
        result.feilendeBaksystem.`should be empty`()
    }

    @Test
    fun `Should link to saksoversikt when there are muliple paabegynte-uris`() {
        val paagebynteUris = listOf(
            "http://paabegynt1",
            "http://paabegynt2",
            "http://paabegynt3"
        )


        val external = PaabegynteSoknaderExternalObjectMother.createPaabegyntSoknad(paagebynteUris, emptyList())

        val result = transformer.toInternal(external)

        result.antallPaabegynte `should be equal to` 3
        result.url `should be equal to` saksoversiktUrl
        result.feilendeBaksystem.`should be empty`()
    }

    @Test
    fun `Should map baksystem as-is`() {
        val feilendeBaksystem = listOf(
            "baksystem1",
            "baksystem2",
            "baksystem3",
        )


        val external = PaabegynteSoknaderExternalObjectMother.createPaabegyntSoknad(emptyList(), feilendeBaksystem)

        val result = transformer.toInternal(external)

        result.antallPaabegynte `should be equal to` 0
        result.url `should be equal to` null
        result.feilendeBaksystem.size `should be equal to` 3
        result.feilendeBaksystem `should be equal to` feilendeBaksystem
    }

    private fun <T> List<T>.`should be empty`() {
        isEmpty() `should be equal to` true
    }
}
