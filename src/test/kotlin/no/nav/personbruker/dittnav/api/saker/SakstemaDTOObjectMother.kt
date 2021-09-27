package no.nav.personbruker.dittnav.api.saker

import java.net.URL
import java.time.ZonedDateTime

object SakstemaDTOObjectMother {

    fun giveMeTemaDagpenger(): SakstemaDTO {
        return SakstemaDTO(
            "Dagpenger",
            "DAG",
            ZonedDateTime.now().minusDays(9),
            URL("https://dummy/DAG")
        )
    }

}