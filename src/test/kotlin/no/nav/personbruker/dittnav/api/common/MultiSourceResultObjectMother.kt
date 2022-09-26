package no.nav.personbruker.dittnav.api.common

import no.nav.personbruker.dittnav.api.beskjed.BeskjedDTO
import no.nav.personbruker.dittnav.api.beskjed.KildeType
import no.nav.personbruker.dittnav.api.beskjed.createActiveBeskjedDto
import no.nav.personbruker.dittnav.api.oppgave.OppgaveDTO
import java.time.ZonedDateTime

object MultiSourceResultObjectMother {

    fun giveMeNumberOfSuccessfulBeskjedEventsForSource(numberOfEvents: Int, source: KildeType, baseEventId : String = "beskjed"): MultiSourceResult<BeskjedDTO, KildeType> {
        val events = mutableListOf<BeskjedDTO>()
        for(lopenummer in 0 until numberOfEvents) {
            events.add(createActiveBeskjedDto("$baseEventId-$lopenummer"))
        }
        return MultiSourceResult.createSuccessfulResult(
            events,
            source
        )
    }

    fun giveMeNumberOfSuccessfulOppgaveEventsForSource(numberOfEvents: Int, source: KildeType, baseEventId : String = "oppgave"): MultiSourceResult<OppgaveDTO, KildeType> {
        val events = mutableListOf<OppgaveDTO>()
        for(lopenummer in 0 until numberOfEvents) {
            events.add(createActiveOppgave("$baseEventId-$lopenummer"))
        }
        return MultiSourceResult.createSuccessfulResult(
            events,
            source
        )
    }

}

fun createActiveOppgave(eventId: String): OppgaveDTO {
    return OppgaveDTO(
        forstBehandlet = ZonedDateTime.now(),
        eventId = eventId,
        tekst = "Dummytekst",
        link = "https://dummy.url",
        produsent = "dummy-produsent",
        sistOppdatert = ZonedDateTime.now().minusDays(3),
        sikkerhetsnivaa = 3,
        aktiv = true,
        grupperingsId = "321",
        eksternVarslingSendt = true,
        eksternVarslingKanaler = listOf("SMS", "EPOST")
    )
}