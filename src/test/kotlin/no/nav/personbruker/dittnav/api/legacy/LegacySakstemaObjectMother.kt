package no.nav.personbruker.dittnav.api.legacy

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
        1,
        ZonedDateTime.now().minusDays(4)
    )

}
