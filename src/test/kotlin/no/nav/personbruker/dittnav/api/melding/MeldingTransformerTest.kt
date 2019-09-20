package no.nav.personbruker.dittnav.api.melding

import no.nav.personbruker.dittnav.api.utils.EventObjectMother
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import kotlin.test.assertEquals

object MeldingTransformerTest : Spek({
    val meldingTransformer = MeldingTransformer

    describe("Transformerer liste av Events til liste av Meldinger") {

        it("filtrerer vekk inaktive events") {
            val eventListe = listOf(EventObjectMother.medAktivtEvent, EventObjectMother.medInaktivtEvent)
            val resultat = meldingTransformer.toOutbound(eventListe)
            assertEquals(expected = 1, actual = resultat.size)
            assertEquals(expected = "1", actual = resultat[0].id)
        }

        it("sender videre riktig data i riktig felt") {
            val eventListe = listOf(EventObjectMother.medAktivtEvent)
            val resultat = meldingTransformer.toOutbound(eventListe)
            assertEquals(eventListe[0].eventId, resultat[0].id)
            assertEquals(eventListe[0].sikkerhetsnivaa, resultat[0].sikkerhetsnivaa)
            assertEquals(eventListe[0].tekst, resultat[0].tekst)
            assertEquals(eventListe[0].sistOppdatert, resultat[0].sistOppdatert)
            assertEquals(eventListe[0].link, resultat[0].link)
        }
    }
})
