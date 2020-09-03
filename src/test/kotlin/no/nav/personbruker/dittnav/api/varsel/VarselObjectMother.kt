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
        LocalDate.of(2020, 8, 8),
        LocalDate.of(2020, 8, 9)
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
        LocalDate.of(2020, 9, 10),
        null
    )
}
