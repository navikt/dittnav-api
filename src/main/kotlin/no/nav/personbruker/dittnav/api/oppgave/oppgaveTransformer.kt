package no.nav.personbruker.dittnav.api.oppgave

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
            return maskedOppgaveDTO.copy(tekst = "***", link = "***", produsent = "***")
        }
