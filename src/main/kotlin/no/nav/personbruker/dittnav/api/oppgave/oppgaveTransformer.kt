package no.nav.personbruker.dittnav.api.oppgave

fun toOppgaveDTO(inbound: Oppgave): OppgaveDTO =
        inbound.let {
            OppgaveDTO(
                    eventTidspunkt = it.eventTidspunkt,
                    eventId = it.eventId,
                    tekst = it.tekst,
                    link = it.link,
                    sistOppdatert = it.sistOppdatert
            )
        }
