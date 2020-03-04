package no.nav.personbruker.dittnav.api.oppgave

fun toOppgaveDTO(inbound: Oppgave): OppgaveDTO =
        inbound.let {
            OppgaveDTO(
                    eventId = it.eventId,
                    produsent = it.produsent,
                    eventTidspunkt = it.eventTidspunkt,
                    link = it.link,
                    sistOppdatert = it.sistOppdatert,
                    tekst = it.tekst
            )
        }
