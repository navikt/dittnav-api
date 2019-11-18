package no.nav.personbruker.dittnav.api.melding

import no.nav.personbruker.dittnav.api.event.Event
import no.nav.personbruker.dittnav.api.event.EventType

object MeldingTransformer {

    fun toOutbound(inbound: List<Event>): List<Melding> {
        return inbound
            .map {
                Melding(
                    id = it.eventId,
                    type = EventType.INFORMASJON,
                    link = it.link,
                    sikkerhetsnivaa = it.sikkerhetsnivaa,
                    sistOppdatert = it.sistOppdatert,
                    tekst = it.tekst
                )
        }
    }
}
