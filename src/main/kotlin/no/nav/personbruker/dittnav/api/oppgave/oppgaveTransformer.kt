package no.nav.personbruker.dittnav.api.oppgave

import no.nav.personbruker.dittnav.api.brukernotifikasjon.Brukernotifikasjon
import no.nav.personbruker.dittnav.api.brukernotifikasjon.BrukernotifikasjonType

fun toBrukernotifikasjon(inbound: Oppgave): Brukernotifikasjon {
    return inbound.let {
                Brukernotifikasjon(
                        uid = 0,
                        eventId = it.eventId,
                        type = BrukernotifikasjonType.OPPGAVE,
                        produsent = it.produsent,
                        eventTidspunkt = it.eventTidspunkt,
                        link = it.link,
                        sistOppdatert = it.sistOppdatert,
                        tekst = it.tekst
                )
            }
}
