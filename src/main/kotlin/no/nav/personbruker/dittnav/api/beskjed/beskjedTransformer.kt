package no.nav.personbruker.dittnav.api.beskjed

import no.nav.personbruker.dittnav.api.brukernotifikasjon.Brukernotifikasjon
import no.nav.personbruker.dittnav.api.brukernotifikasjon.BrukernotifikasjonType

fun toBrukernotifikasjon(inbound: Beskjed): Brukernotifikasjon {
    return inbound.let{
            Brukernotifikasjon(
                    eventId = it.eventId,
                    type = BrukernotifikasjonType.BESKJED,
                    produsent = it.produsent,
                    eventTidspunkt = it.eventTidspunkt,
                    link = it.link,
                    sistOppdatert = it.sistOppdatert,
                    tekst = it.tekst
            )
    }
}