package no.nav.personbruker.dittnav.api.informasjon

import no.nav.personbruker.dittnav.api.brukernotifikasjon.Brukernotifikasjon
import no.nav.personbruker.dittnav.api.config.EventType

object InformasjonTransformer {

    fun toBrukernotifikasjonList(inbound: List<Informasjon>): List<Brukernotifikasjon> {
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
