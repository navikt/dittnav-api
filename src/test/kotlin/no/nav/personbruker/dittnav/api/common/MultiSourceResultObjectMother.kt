package no.nav.personbruker.dittnav.api.common

import no.nav.personbruker.dittnav.api.beskjed.BeskjedDTO
import no.nav.personbruker.dittnav.api.beskjed.BeskjedDtoObjectMother
import no.nav.personbruker.dittnav.api.beskjed.KildeType

object MultiSourceResultObjectMother {

    fun giveMeNumberOfSuccessEventsForSource(numberOfEvents: Int, source: KildeType, baseEventId : String = "beskjed"): MultiSourceResult<BeskjedDTO, KildeType> {
        val events = mutableListOf<BeskjedDTO>()
        for(lopenummer in 0 until numberOfEvents) {
            events.add(BeskjedDtoObjectMother.createActiveBeskjed("$baseEventId-$lopenummer"))
        }
        return MultiSourceResult.createSuccessfulResult(
            events,
            source
        )
    }

}
