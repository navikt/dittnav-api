package no.nav.personbruker.dittnav.api.oppgave

import no.nav.personbruker.dittnav.api.brukernotifikasjon.Brukernotifikasjon
import no.nav.personbruker.dittnav.api.brukernotifikasjon.BrukernotifikasjonType

fun toOppgaveDTO(inbound: Oppgave): OppgaveDTO =
        inbound.let {
            OppgaveDTO(
                    eventTidspunkt = it.eventTidspunkt,
                    eventId = it.eventId,
                    tekst = it.tekst,
                    link = it.link,
                    produsent = it.produsent,
                    sistOppdatert = it.sistOppdatert,
                    sikkerhetsnivaa = it.sikkerhetsnivaa
            )
        }

fun toMaskedOppgaveDTO(oppgave: Oppgave): OppgaveDTO =
        oppgave.let {
            var maskedOppgaveDTO = toOppgaveDTO(oppgave)
            return maskedOppgaveDTO.copy(tekst = "***", link = "***")
        }

fun toBrukernotifikasjon(inbound: Oppgave): Brukernotifikasjon =
        inbound.let {
            Brukernotifikasjon(
                    eventId = it.eventId,
                    type = BrukernotifikasjonType.OPPGAVE,
                    eventTidspunkt = it.eventTidspunkt,
                    link = it.link,
                    sistOppdatert = it.sistOppdatert,
                    tekst = it.tekst,
                    sikkerhetsnivaa = it.sikkerhetsnivaa
            )
        }
