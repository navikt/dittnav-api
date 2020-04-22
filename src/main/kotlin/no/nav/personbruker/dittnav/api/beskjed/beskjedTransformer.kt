package no.nav.personbruker.dittnav.api.beskjed

import no.nav.personbruker.dittnav.api.brukernotifikasjon.Brukernotifikasjon
import no.nav.personbruker.dittnav.api.brukernotifikasjon.BrukernotifikasjonType

fun toBeskjedDTO(beskjed: Beskjed): BeskjedDTO =
        beskjed.let {
            BeskjedDTO(
                    uid = it.uid,
                    eventTidspunkt = it.eventTidspunkt,
                    eventId = it.eventId,
                    tekst = it.tekst,
                    link = it.link,
                    produsent = it.produsent,
                    sistOppdatert = it.sistOppdatert,
                    sikkerhetsnivaa = it.sikkerhetsnivaa
            )
        }

fun toMaskedBeskjedDTO(beskjed: Beskjed): BeskjedDTO =
        beskjed.let {
            var maskedBeskjedDTO = toBeskjedDTO(beskjed)
            return maskedBeskjedDTO.copy(tekst = "***", link = "***")
        }

fun toBrukernotifikasjon(beskjed: Beskjed): Brukernotifikasjon =
    beskjed.let{
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
