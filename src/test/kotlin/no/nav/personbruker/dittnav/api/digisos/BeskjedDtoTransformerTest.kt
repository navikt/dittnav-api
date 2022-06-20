package no.nav.personbruker.dittnav.api.digisos

import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class BeskjedDtoTransformerTest {

    @Test
    fun `Skal kunne konvertere til intern modell`() {
        val external = PaabegynteObjectMother.giveMeOne()

        val internal = external.toInternal()

        internal.eventId shouldBe external.eventId
        internal.forstBehandlet shouldBe external.eventTidspunkt.toZonedDateTime()
        internal.grupperingsId shouldBe external.grupperingsId
        internal.tekst shouldBe external.tekst
        internal.link shouldBe external.link
        internal.aktiv shouldBe external.isAktiv
        internal.sistOppdatert shouldBe external.sistOppdatert.toZonedDateTime()
        internal.produsent shouldBe "digiSos"
    }

    @Test
    fun `Skal konvertere flere eksterne til interne samtidig`() {
        val externals = listOf(
            PaabegynteObjectMother.giveMeOne(true),
            PaabegynteObjectMother.giveMeOne(false)
        )

        val internals = externals.toInternals()

        internals.shouldNotBeNull()
        internals.size shouldBe externals.size
        internals[0].shouldNotBeNull()
        internals[1].shouldNotBeNull()
    }

    @Test
    fun `Skal kappe tekster som er for lange`() {
        val eventMedForLangTekst = PaabegynteObjectMother.giveMeOne().copy(
            tekst = "A".repeat(maxBeskjedTextLength + 1)
        )

        val internal = eventMedForLangTekst.toInternal()

        internal.tekst.length shouldBe maxBeskjedTextLength
        internal.tekst.endsWith("...") shouldBe true
    }

}
