package no.nav.personbruker.dittnav.api.saker.ekstern

import java.time.ZonedDateTime

object SisteSakstemaerObjectMother {

    fun giveMeSisteSakstemaer(): SisteSakstemaer {
        return SisteSakstemaer(
            SakstemaObjectMother.giveMeListe(),
            ZonedDateTime.now()
        )
    }

}
