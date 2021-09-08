package no.nav.personbruker.dittnav.api.digisos

import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.shouldNotBeNull
import org.junit.jupiter.api.Test

internal class OppgaveDtoTransformerTest {

    @Test
    fun `Skal kunne konvertere til intern modell`() {
        val external = EttersendelseObjectMother.giveMeOne()

        val internal = external.toInternal()

        internal.eventId `should be equal to` external.eventId
        internal.eventTidspunkt `should be equal to` external.eventTidspunkt.toZonedDateTime()
        internal.grupperingsId `should be equal to` external.grupperingsId
        internal.tekst `should be equal to` external.tekst
        internal.link `should be equal to` external.link
        internal.aktiv `should be equal to` external.aktiv
        internal.sistOppdatert `should be equal to` external.sistOppdatert.toZonedDateTime()
        internal.produsent `should be equal to` "digiSos"
        internal.uid `should be equal to` external.eventId
    }

    @Test
    fun `Skal konvertere flere eksterne til interne samtidig`() {
        val externals = listOf(
            PaabegynteObjectMother.giveMeOne(true),
            PaabegynteObjectMother.giveMeOne(false)
        )

        val internals = externals.toInternals()

        internals.shouldNotBeNull()
        internals.size `should be equal to` externals.size
        internals[0].shouldNotBeNull()
        internals[1].shouldNotBeNull()
    }

    @Test
    fun `Skal kappe tekster som er for lange`() {
        val eventMedForLangTekst = EttersendelseObjectMother.giveMeOne().copy(
            tekst = "A".repeat(maxOppgaveTextLength + 1)
        )

        val internal = eventMedForLangTekst.toInternal()

        internal.tekst.length `should be equal to` maxOppgaveTextLength
        internal.tekst.endsWith("...") `should be equal to` true
    }

}
