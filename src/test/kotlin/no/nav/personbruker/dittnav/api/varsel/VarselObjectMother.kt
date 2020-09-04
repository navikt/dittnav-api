package no.nav.personbruker.dittnav.api.varsel

import java.time.LocalDate

fun createInactiveVarsel(eventId: String): Varsel {
    return Varsel(
        1,
        "2",
        "https://en.url",
        "Tekst",
        eventId,
        "3",
        LocalDate.now().minusDays(30),
        LocalDate.now().minusDays(20)
    )
}

fun createActiveVarsel(eventId: String): Varsel {
    return Varsel(
        2,
        "3",
        "https://en.url",
        "Tekst",
        eventId,
        "4",
        LocalDate.now().minusDays(10),
        null
    )
}
