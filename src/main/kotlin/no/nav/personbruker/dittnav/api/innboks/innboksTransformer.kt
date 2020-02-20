package no.nav.personbruker.dittnav.api.innboks

import no.nav.personbruker.dittnav.api.brukernotifikasjon.Brukernotifikasjon
import no.nav.personbruker.dittnav.api.brukernotifikasjon.BrukernotifikasjonType

fun toBrukernotifikasjon(innboks: Innboks): Brukernotifikasjon =
        innboks.let {
            Brukernotifikasjon(
                    uid = 0,
                    eventId = it.eventId,
                    type = BrukernotifikasjonType.INNBOKS,
                    produsent = it.produsent,
                    eventTidspunkt = it.eventTidspunkt,
                    link = it.link,
                    sistOppdatert = it.sistOppdatert,
                    tekst = it.tekst
            )
        }