package no.nav.personbruker.dittnav.api.beskjed

import no.nav.personbruker.dittnav.api.brukernotifikasjon.Brukernotifikasjon
import no.nav.personbruker.dittnav.api.brukernotifikasjon.BrukernotifikasjonType

fun toBeskjedDTO(inbound: Beskjed): BeskjedDTO =
        inbound.let {
            BeskjedDTO(
                    uid = it.uid,
                    eventTidspunkt = it.eventTidspunkt,
                    eventId = it.eventId,
                    tekst = it.tekst,
                    link = it.link,
                    sistOppdatert = it.sistOppdatert,
                    sikkerhetsnivaa = it.sikkerhetsnivaa
            )
        }

fun toBrukernotifikasjon(inbound: Beskjed): Brukernotifikasjon {
    return inbound.let{
        Brukernotifikasjon(
                uid = it.uid,
                eventId = it.eventId,
                type = BrukernotifikasjonType.BESKJED,
                eventTidspunkt = it.eventTidspunkt,
                link = it.link,
                sistOppdatert = it.sistOppdatert,
                tekst = it.tekst,
                sikkerhetsnivaa = it.sikkerhetsnivaa
        )
    }
}
