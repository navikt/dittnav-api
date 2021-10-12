package no.nav.personbruker.dittnav.api.saker

import no.nav.personbruker.dittnav.api.saker.ekstern.SisteSakstemaer
import java.time.ZonedDateTime

object SisteSakstemaerObjectMother {

    fun giveMeSisteSakstemaer(): SisteSakstemaer {
        return SisteSakstemaer(
            SakstemaObjectMother.giveMeListe(),
            ZonedDateTime.now()
        )
    }

}