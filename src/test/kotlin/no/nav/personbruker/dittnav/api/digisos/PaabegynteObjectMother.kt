package no.nav.personbruker.dittnav.api.digisos

import java.time.LocalDateTime

object PaabegynteObjectMother {

    fun giveMeOne() = Paabegynte(
        LocalDateTime.now(),
        "123",
        "Dette er en dummytekst",
        "https://nav.no/lenke",
        4,
        LocalDateTime.now(),
        false
    )

}
