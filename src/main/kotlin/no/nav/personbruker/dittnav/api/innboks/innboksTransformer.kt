package no.nav.personbruker.dittnav.api.innboks

import no.nav.personbruker.dittnav.api.brukernotifikasjon.Brukernotifikasjon
import no.nav.personbruker.dittnav.api.brukernotifikasjon.BrukernotifikasjonType

fun toInnboksDTO(innboks: Innboks): InnboksDTO =
        innboks.let {
            InnboksDTO(
                    eventTidspunkt = it.eventTidspunkt,
                    eventId = it.eventId,
                    tekst = it.tekst,
                    link = it.link,
                    sistOppdatert = it.sistOppdatert,
                    sikkerhetsnivaa = it.sikkerhetsnivaa
            )
        }

fun toMaskedInnboksDTO(innboks: Innboks): InnboksDTO =
        innboks.let {
            var maskedInnboksDTO = toInnboksDTO(innboks)
            return maskedInnboksDTO.copy(tekst = "***", link = "***")
        }

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
