package no.nav.personbruker.dittnav.api.informasjon

import no.nav.personbruker.dittnav.api.brukernotifikasjon.Brukernotifikasjon
import no.nav.personbruker.dittnav.api.event.EventType

object InformasjonTransformer {

    fun toBrukernotifikasjon(inbound: List<Informasjon>): List<Brukernotifikasjon> {
        return inbound
            .map {
                Brukernotifikasjon(
                        eventId = it.eventId,
                        type = EventType.INFORMASJON,
                        produsent = it.produsent,
                        eventTidspunkt = it.eventTidspunkt,
                        link = it.link,
                        sistOppdatert = it.sistOppdatert,
                        tekst = it.tekst
                )
        }
    }
}
