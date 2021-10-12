package no.nav.personbruker.dittnav.api.saker.ekstern

import no.nav.personbruker.dittnav.api.saker.Sakstema
import java.net.URL
import java.time.ZonedDateTime

object SakstemaObjectMother {

    fun giveMeSakstemaDagpenger() = Sakstema(
        "Dagpenger",
        "DAG",
        ZonedDateTime.now().minusDays(8),
        URL("https://dummy/DAG")
    )

    fun giveMeSakstemaSosialhjelp() = Sakstema(
        "Økonomisk sosialhjelp",
        "KOM",
        ZonedDateTime.now().minusDays(24),
        URL("https://dummy/KOM")
    )

    fun giveMeListe() = listOf(giveMeSakstemaDagpenger(), giveMeSakstemaSosialhjelp())

}
