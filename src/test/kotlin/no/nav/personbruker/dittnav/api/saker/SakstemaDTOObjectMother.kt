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

    fun giveMeTemaBil(): SakstemaDTO {
        return SakstemaDTO(
            "Bil",
            "BIL",
            ZonedDateTime.now().minusDays(2),
            URL("https://dummy/BIL")
        )
    }

    fun giveMeList(): List<SakstemaDTO> {
        return listOf(giveMeTemaDagpenger(), giveMeTemaBil())
    }

}
