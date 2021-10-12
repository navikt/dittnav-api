package no.nav.personbruker.dittnav.api.saker

import java.time.ZonedDateTime

object SisteSakstemaerDtoObjectMother {

    fun giveMeTemaDagpenger(): SisteSakstemaerDTO {
        return SisteSakstemaerDTO(
            SakstemaDTOObjectMother.giveMeList(),
            ZonedDateTime.now()
        )
    }

}
