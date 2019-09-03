package no.nav.personbruker.dittnav.api.meldinger

import no.nav.personbruker.dittnav.api.domain.Event
import no.nav.personbruker.dittnav.api.domain.Melding

class MeldingTransformer {

    fun toOutbound(inbound: List<Event>): List<Melding> {
        return inbound.map {
            Melding(
                id = it.eventId,
                link = it.link,
                sikkerhetsnivaa = it.sikkerhetsnivaa,
                sistOppdatert = it.sistOppdatert,
                tekst = it.tekst
            )
        }
    }
}
