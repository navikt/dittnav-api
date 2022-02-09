package no.nav.personbruker.dittnav.api.saker

import java.net.URL
import java.time.ZonedDateTime

object SakerDtoObjectMother {

    fun giveMeSisteSakstemaForDev() = SakerDTO(
        listOf(
            SakstemaDTOObjectMother.giveMeTemaDagpenger(),
            SakstemaDTOObjectMother.giveMeTemaBil()
        ),
        URL("https://person.dev.nav.no/mine-saker"),
        ZonedDateTime.now().minusDays(2)
    )

}
