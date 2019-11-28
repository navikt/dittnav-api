package no.nav.personbruker.dittnav.api.informasjon

import no.nav.personbruker.dittnav.api.brukernotifikasjon.Brukernotifikasjon
import no.nav.personbruker.dittnav.api.brukernotifikasjon.BrukernotifikasjonType

fun toBrukernotifikasjon(inbound: Informasjon): Brukernotifikasjon {
    return inbound.let{
            Brukernotifikasjon(
                    eventId = it.eventId,
                    type = BrukernotifikasjonType.INFORMASJON,
                    produsent = it.produsent,
                    eventTidspunkt = it.eventTidspunkt,
                    link = it.link,
                    sistOppdatert = it.sistOppdatert,
                    tekst = it.tekst
            )
    }
}
