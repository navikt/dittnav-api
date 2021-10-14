package no.nav.personbruker.dittnav.api.legacy.saksoversikt

import java.time.ZonedDateTime

object LegacySakstemaObjectMother {

    fun giveMeSakstemaDagpenger() = LegacySakstema(
        "Dagpenger",
        "DAG",
        1,
        ZonedDateTime.now().minusDays(23)
    )

    fun giveMeSakstemaOppfolging() = LegacySakstema(
        "Oppfølging",
        "OPP",
        2,
        ZonedDateTime.now().minusDays(4)
    )

    fun giveMeSakstemaBil() = LegacySakstema(
        "Bil",
        "BIL",
        3,
        ZonedDateTime.now().minusMinutes(40)
    )

    fun giveMeSakstemaSosialhjelp() = LegacySakstema(
        "Økonomisk sosialhjelp",
        "KOM",
        4,
        ZonedDateTime.now().minusMonths(5)
    )

}
