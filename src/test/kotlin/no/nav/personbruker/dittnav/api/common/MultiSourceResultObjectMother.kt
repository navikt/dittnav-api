package no.nav.personbruker.dittnav.api.common

import no.nav.personbruker.dittnav.api.beskjed.BeskjedDTO
import no.nav.personbruker.dittnav.api.beskjed.BeskjedDtoObjectMother
import no.nav.personbruker.dittnav.api.beskjed.KildeType
import no.nav.personbruker.dittnav.api.oppgave.OppgaveDTO
import no.nav.personbruker.dittnav.api.oppgave.OppgaveDtoObjectMother

object MultiSourceResultObjectMother {

    fun giveMeNumberOfSuccessfulBeskjedEventsForSource(numberOfEvents: Int, source: KildeType, baseEventId : String = "beskjed"): MultiSourceResult<BeskjedDTO, KildeType> {
        val events = mutableListOf<BeskjedDTO>()
        for(lopenummer in 0 until numberOfEvents) {
            events.add(BeskjedDtoObjectMother.createActiveBeskjed("$baseEventId-$lopenummer"))
        }
        return MultiSourceResult.createSuccessfulResult(
            events,
            source
        )
    }

    fun giveMeNumberOfSuccessfulOppgaveEventsForSource(numberOfEvents: Int, source: KildeType, baseEventId : String = "oppgave"): MultiSourceResult<OppgaveDTO, KildeType> {
        val events = mutableListOf<OppgaveDTO>()
        for(lopenummer in 0 until numberOfEvents) {
            events.add(OppgaveDtoObjectMother.createActiveOppgave("$baseEventId-$lopenummer"))
        }
        return MultiSourceResult.createSuccessfulResult(
            events,
            source
        )
    }

}
