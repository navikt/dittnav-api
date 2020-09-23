package no.nav.personbruker.dittnav.api.varsel

import java.time.ZonedDateTime

fun createLestVarsel(eventId: String): Varsel {
    return Varsel(
        1,
        "2",
        "https://en.url",
        "Tekst",
        eventId,
        "3",
        ZonedDateTime.now().minusDays(30),
        ZonedDateTime.now().minusDays(20)
    )
}

fun createUlestVarsel(eventId: String): Varsel {
    return Varsel(
        2,
        "3",
        "https://en.url",
        "Tekst",
        eventId,
        "4",
        ZonedDateTime.now().minusDays(10),
        null
    )
}
