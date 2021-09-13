package no.nav.personbruker.dittnav.api.digisos

import java.time.LocalDateTime

object PaabegynteObjectMother {

    fun giveMeOne(active: Boolean = false) = Paabegynte(
        LocalDateTime.now(),
        "123",
        "987",
        "Dette er en dummytekst",
        "https://nav.no/lenke",
        4,
        LocalDateTime.now(),
        active
    )

}
