package no.nav.personbruker.dittnav.api.digisos

import org.amshove.kluent.`should be equal to`
import org.junit.jupiter.api.Test

internal class DigiSosTransformerTest {

    @Test
    fun `Skal kunne konvertere til intern modell`() {
        val external = PaabegynteObjectMother.giveMeOne()

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

}
