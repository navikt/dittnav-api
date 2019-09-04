package no.nav.personbruker.dittnav.api.utils

import no.nav.personbruker.dittnav.api.domain.Event

object EventObjectMother {

    val medAktivtEvent = Event(
        aktiv = true,
        aktorId = "1",
        dokumentId = "1",
        eventId = "1",
        eventTidspunkt = "1",
        id = "1",
        link = "https://dummyURL.com",
        produsent = "11",
        sikkerhetsnivaa = 4,
        sistOppdatert = "1",
        tekst = "Dette er en test"
    )

    val medInaktivtEvent = Event(
        aktiv = false,
        aktorId = "1",
        dokumentId = "1",
        eventId = "1",
        eventTidspunkt = "1",
        id = "2",
        link = "https://dummyURL.com",
        produsent = "11",
        sikkerhetsnivaa = 4,
        sistOppdatert = "1",
        tekst = "Dette er en test"
    )
}
