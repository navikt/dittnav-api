package no.nav.personbruker.dittnav.api.saker

import java.net.URL
import java.time.ZonedDateTime

object SakstemaObjectMother {

    fun giveMeSakstemaDagpenger() = Sakstema(
        "Dagpenger",
        "DAG",
        ZonedDateTime.now().minusDays(8),
        URL("https://dummy/DAG")
    )

}
