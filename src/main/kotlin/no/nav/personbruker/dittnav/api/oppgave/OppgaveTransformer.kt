package no.nav.personbruker.dittnav.api.oppgave

import no.nav.personbruker.dittnav.api.brukernotifikasjon.Brukernotifikasjon
import no.nav.personbruker.dittnav.api.brukernotifikasjon.BrukernotifikasjonType

object OppgaveTransformer {

    fun toBrukernotifikasjonList(inbound: List<Oppgave>): List<Brukernotifikasjon> {
        return inbound
                .map {
                    Brukernotifikasjon(
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
}
