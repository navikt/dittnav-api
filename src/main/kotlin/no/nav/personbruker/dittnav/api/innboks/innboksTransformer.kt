package no.nav.personbruker.dittnav.api.innboks

import no.nav.personbruker.dittnav.api.brukernotifikasjon.Brukernotifikasjon
import no.nav.personbruker.dittnav.api.brukernotifikasjon.BrukernotifikasjonType

fun toBrukernotifikasjon(innboks: Innboks): Brukernotifikasjon =
        innboks.let {
            Brukernotifikasjon(
                    eventId = it.eventId,
                    type = BrukernotifikasjonType.INNBOKS,
                    eventTidspunkt = it.eventTidspunkt,
                    link = it.link,
                    sistOppdatert = it.sistOppdatert,
                    tekst = it.tekst,
                    sikkerhetsnivaa = it.sikkerhetsnivaa
            )
        }
