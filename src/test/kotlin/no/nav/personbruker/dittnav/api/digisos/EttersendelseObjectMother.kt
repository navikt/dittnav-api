package no.nav.personbruker.dittnav.api.digisos

import java.time.LocalDateTime

object EttersendelseObjectMother {

    fun giveMeOne(active: Boolean = false) = Ettersendelse(
        "123",
        LocalDateTime.now(),
        "987",
        "Dette er en dummytekst",
        "https://nav.no/lenke",
        4,
        LocalDateTime.now().minusDays(1),
        active
    )

}
