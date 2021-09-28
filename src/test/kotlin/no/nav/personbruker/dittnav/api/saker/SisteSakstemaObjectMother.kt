package no.nav.personbruker.dittnav.api.saker

import java.net.URL

object SisteSakstemaObjectMother {

    fun giveMeSisteSakstemaForDev() = SisteSakstemaDTO(
        listOf(
            SakstemaDTOObjectMother.giveMeTemaDagpenger(),
            SakstemaDTOObjectMother.giveMeTemaBil()
        ),
        URL("https://person.dev.nav.no/mine-saker")
    )

}
